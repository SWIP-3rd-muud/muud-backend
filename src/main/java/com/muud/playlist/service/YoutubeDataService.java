package com.muud.playlist.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.muud.emotion.domain.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.domain.dto.PlayListRequest;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class YoutubeDataService {

    private String apiKey;
    private final PlayListRepository playListRepository;
    private final YouTube youtube;

    @Autowired
    public YoutubeDataService(PlayListRepository playListRepository, @Value("${youtube.api-key}") String apiKey) {
        this.playListRepository = playListRepository;
        this.apiKey = apiKey;
        this.youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {})
                .setApplicationName("your-application-name")
                .build();
    }

    //비디오 데이터 추가, 이미 DB에 존재하면 스킵
    @Transactional
    public int upsertPlayList() throws IOException {
        log.info("===playlist data upsert job start===");
        List<PlayList> playLists = new ArrayList<>();
        Set<String> idSet = new HashSet<>();

        for (Emotion emotion : Emotion.values()) {
            List<String> ids = fetchVideoIdsByEmotion(emotion);
            ids.removeIf(idSet::contains);  // 중복 제거
            idSet.addAll(ids);
            playLists.addAll(getVideoDetails(emotion, ids));
        }

        return savePlayList(playLists).size();
    }

    protected List<String> fetchVideoIdsByEmotion(Emotion emotion) throws IOException {
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id"));
        search.setKey(apiKey)
                .setMaxResults(30L)
                .setOrder("rating")
                .setType(Collections.singletonList("video"))
                .setVideoEmbeddable("true")
                .setVideoDuration("long")
                .setQ(SearchQuery.getQueryByEmotion(emotion) + SearchFilter.EXCLUDE_ALL);

        SearchListResponse searchResponse = search.execute();
        return searchResponse.getItems().stream()
                .map(p -> p.getId().getVideoId())
                .collect(Collectors.toList());
    }

    @Transactional
    public void addPlayList(PlayListRequest playListRequestList) {
        playListRequestList.getPlayLists().forEach((emotion, videoIds) -> {
            try {
                List<PlayList> playLists = getVideoDetails(emotion, videoIds);
                savePlayList(playLists);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ApiException(ExceptionType.SYSTEM_ERROR);
            }
        });
    }

    private List<PlayList> savePlayList(List<PlayList> playListList) {
        List<PlayList> playLists = new ArrayList<>();
        playListList.forEach(p -> {
            PlayList modifiedPlaylist = playListRepository.findByVideoId(p.getVideoId())
                    .map(existingPlaylist -> {
                        existingPlaylist.updateDetails(p.getTitle(), p.getChannelName(), p.getTags());
                        return existingPlaylist;
                    })
                    .orElse(p);
            playLists.add(modifiedPlaylist);
        });
        return playListRepository.saveAll(playLists);
    }

    public List<PlayList> getVideoDetails(Emotion emotion, List<String> videoIds) throws IOException {
        if (videoIds.isEmpty()) return Collections.emptyList();

        YouTube.Videos.List request = youtube.videos().list(Collections.singletonList("snippet"));
        request.setKey(apiKey).setId(videoIds);
        List<Video> videoList = request.execute().getItems();
        return videoList.stream().map(video -> {
            VideoSnippet snippet = video.getSnippet();
            return PlayList.builder()
                    .videoId(video.getId())
                    .title(snippet.getTitle())
                    .channelName(snippet.getChannelTitle())
                    .tags(snippet.getTags())
                    .emotion(emotion)
                    .build();
        }).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 4 */2 * *")
    @Transactional
    public void checkAndRemoveDeletedVideos() throws IOException {
        //모든 플레이리스트에 대해 수행
        List<String> allVideoIds = getAllVideoIds();
        List<String> deletedVideoIds = new ArrayList<>();

        YouTube.Videos.List request = youtube.videos().list(Collections.singletonList("id"));
        request.setKey(apiKey);

        //50개씩 나눠서 수행
        for (List<String> videoIdsBatch : partitionList(allVideoIds, 50)) {
            request.setId(videoIdsBatch);
            List<Video> videos = request.execute().getItems();

            Set<String> existingVideoIds = videos.stream()
                    .map(Video::getId)
                    .collect(Collectors.toSet());

            for (String videoId : videoIdsBatch) {
                if (!existingVideoIds.contains(videoId)) {
                    deletedVideoIds.add(videoId);
                }
            }
        }

        if (!deletedVideoIds.isEmpty()) {
            playListRepository.deleteByVideoIds(deletedVideoIds);
        }
    }

    public List<String> getAllVideoIds() {
        return playListRepository.findAllVideoIds();
    }

    private List<List<String>> partitionList(List<String> list, int size) {
        List<List<String>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}
