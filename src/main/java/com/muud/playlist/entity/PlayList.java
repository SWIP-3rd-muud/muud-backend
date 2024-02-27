package com.muud.playlist.entity;

import com.google.api.services.youtube.model.SearchResult;
import com.muud.emotion.entity.Emotion;
import com.muud.global.common.BaseEntity;
import com.muud.playlist.dto.VideoDto;
import com.muud.playlist.repository.PlayListRepository;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayList extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long id;
    private String title;
    private String videoId;
    private String channelName;
    @Enumerated(value = EnumType.STRING)
    private Emotion emotion;

    @Builder
    public PlayList(String title, String videoId, String channelName, Emotion emotion) {
        this.title = title;
        this.videoId = videoId;
        this.channelName = channelName;
        this.emotion = emotion;
    }

    public VideoDto toDto(){
        return VideoDto.builder()
                .id(id)
                .videoId(videoId)
                .title(title)
                .build();
    }
    public static PlayList from(SearchResult searchResult, Emotion emotion){
        return PlayList.builder()
                .title(searchResult.getSnippet().getTitle())
                .videoId(searchResult.getId().getVideoId())
                .channelName(searchResult.getSnippet().getChannelTitle())
                .emotion(emotion)
                .build();
    }
}
