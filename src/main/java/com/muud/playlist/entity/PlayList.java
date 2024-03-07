package com.muud.playlist.entity;

import com.muud.emotion.entity.Emotion;
import com.muud.global.common.BaseEntity;
import com.muud.playlist.dto.PlayListRequest;
import com.muud.playlist.dto.VideoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayList extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long id;
    private String title;
    @Column(unique = true)
    private String videoId;
    private String channelName;
    @Enumerated(value = EnumType.STRING)
    private Emotion emotion;
    private String tags;
    @Builder
    public PlayList(String title, String videoId, String channelName, Emotion emotion, List<String> tags) {
        this.title = title;
        this.videoId = videoId;
        this.channelName = channelName;
        this.emotion = emotion;
        this.tags = convertTagsToString(tags);
    }

    public VideoDto toDto(){
        return VideoDto.builder()
                .id(id)
                .videoId(videoId)
                .title(title)
                .tags(convertTagsToList(tags))
                .channelName(channelName)
                .build();
    }
    public String convertTagsToString(List<String> tagList){
        if(tagList==null) return null;
        if(tagList.size()>3){
            tagList = tagList.subList(0, 3);
        }
        StringBuilder sb = new StringBuilder();
        tagList.forEach(t -> sb.append(t+";"));
        return sb.toString();
    }
    public List<String> convertTagsToList(String tags){
        if(tags!=null)
            return Arrays.stream(tags.split(";")).toList();
        return Collections.EMPTY_LIST;
    }
    public void updateDetails(String title, String channelName, String tags){
        this.title = title;
        this.channelName = channelName;
        this.tags = tags;
    }
}
