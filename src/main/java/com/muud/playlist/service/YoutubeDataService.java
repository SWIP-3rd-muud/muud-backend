package com.muud.playlist.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.muud.emotion.domain.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.dto.PlayListRequest;
import com.muud.playlist.entity.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class YoutubeDataService {
    @Value("${youtube.api-key}")
    private String apiKey;
    private final PlayListRepository playListRepository;
    private final String SEARCH_FILTER = " -교회 -찬양 -찬송";

    private final Map<Emotion, String> queryMap = new HashMap<>();
    @Transactional
    public int updateVideoList() throws IOException {
        log.info("playlist data refresh schedule start");
        JsonFactory jsonFactory = new JacksonFactory();

        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {})
                .build();
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id"));

        // API 설정
        search.setKey(apiKey);
        search.setMaxResults(30l);
        search.setOrder("rating");
        search.setType(Collections.singletonList("video"));
        search.setVideoEmbeddable("true");
        search.setVideoDuration("long");
        List<PlayList> playLists = new ArrayList<>();
        Set<String> idSet = new HashSet<>();
        for(Emotion emotion: Emotion.values()){
            search.setQ(getQueryByEmotion(emotion));
            SearchListResponse searchResponse = search.execute();
            List<String> ids= searchResponse.getItems().stream()
                    .map(p -> p.getId().getVideoId())
                    .filter(id -> !idSet.contains(id))
                    .collect(Collectors.toList());
            idSet.addAll(ids);
            playLists.addAll(getVideoDetails(emotion, ids));
        }
        return savePlayList(playLists).size();
    }
    @Transactional
    public void addPlayList(PlayListRequest playListRequestList){
        playListRequestList.getPlayLists()
                        .forEach((emotion, strings) -> {
                            try {
                                List<PlayList> playLists = getVideoDetails(emotion, strings);
                                savePlayList(playLists);
                            } catch (IOException e) {
                                log.error(e.getMessage());
                                throw new ApiException(ExceptionType.SYSTEM_ERROR);
                            }
                        });
    }
    public List<PlayList> savePlayList(List<PlayList> playListList){
        List<PlayList> playLists = new ArrayList<>();
        playListList.forEach(p -> {
            PlayList modifiedPlaylist = playListRepository.findByVideoId(p.getVideoId())
                    .map(playList -> {
                        playList.updateDetails(p.getTitle(), p.getChannelName(), p.getTags());
                        return playList;
                    })
                    .orElse(p);
            playLists.add(modifiedPlaylist);
        });
        return playListRepository.saveAll(playLists);
    }

    public List<PlayList> getVideoDetails(Emotion emotion, List<String> videoIds) throws IOException {
        if(videoIds.isEmpty()) return null;
        JsonFactory jsonFactory = new JacksonFactory();
        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {})
                .build();
        YouTube.Videos.List request = youtube.videos()
                .list(Collections.singletonList("snippet"));
        request.setKey(apiKey)
                .setId(videoIds);
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

    @PostConstruct
    public void init(){
        queryMap.put(Emotion.JOY, "기쁠 때 들으면 좋은 PlayList");
        queryMap.put(Emotion.SAD, "슬플 때 들으면 좋은 PlayList | 슬픔을 극복할 수 있는 PlayList");
        queryMap.put(Emotion.CALM, "잔잔할 때 들으면 좋은 PlayList | 텐션업 PlayList");
        queryMap.put(Emotion.BLUE, "우울할 때 들으면 좋은 PlayList | 위로가 되는 PlayList");
        queryMap.put(Emotion.ANGER, "화날 때 들으면 좋은 PlayList");
        queryMap.put(Emotion.TIRED, "피곤할 때 들으면 좋은 PlayList | 잠깨는 PlayList");
    }
    public String getQueryByEmotion(Emotion emotion){
        return queryMap.get(emotion) + SEARCH_FILTER;
    }
}
