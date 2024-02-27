package com.muud.playlist.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.muud.emotion.entity.Emotion;
import com.muud.playlist.entity.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class YoutubeDataService {
    @Value("${youtube.api-key}")
    private String apiKey;
    private final PlayListRepository playListRepository;

    @Scheduled(cron = "0 10 0 * * ?", zone = "Asia/Seoul")
    public void updateVideoList() throws IOException {
        log.info("playlist data refresh schedule start");
        // JSON 데이터를 처리하기 위한 JsonFactory 객체 생성
        JsonFactory jsonFactory = new JacksonFactory();
        playListRepository.deleteAll();

        // YouTube 객체를 빌드하여 API에 접근할 수 있는 YouTube 클라이언트 생성
        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {})
                .build();

        // YouTube Search API를 사용하여 동영상 검색을 위한 요청 객체 생성
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("snippet"));

        // API 설정
        search.setKey(apiKey);
        search.setMaxResults(30l);
        search.setOrder("rating");
        search.setType(Collections.singletonList("video"));
        search.setVideoEmbeddable("true");
        search.setVideoDuration("long");
        List<PlayList> playLists = new ArrayList<>();
        for(Emotion emotion: Emotion.values()){
            // 검색어 설정
            search.setQ(emotion.getTitleEmotion()+" PlayList");
            SearchListResponse searchResponse = search.execute();

            // 검색 결과에서 동영상 목록 가져오기
            playLists.addAll(searchResponse.getItems().stream()
                    .map(r -> PlayList.from(r, emotion))
                    .collect(Collectors.toList()));
        }
        savePlayList(playLists);
    }
    public void savePlayList(List<PlayList> playListList){
        playListRepository.saveAll(playListList);
    }

    public Map<String, List<String>> getVideoDetails(List<String> videoIds) throws IOException {
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
        Map<String, List<String>> tagsMap = new HashMap<>();
        videoList.forEach(video -> {
            tagsMap.put(video.getId(), video.getSnippet().getTags());
        });
        return tagsMap;
    }
}
