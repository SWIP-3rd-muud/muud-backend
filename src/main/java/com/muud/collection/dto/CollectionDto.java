package com.muud.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muud.playlist.dto.VideoDto;
import com.muud.playlist.entity.PlayList;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionDto {
    private Long collectionId;
    private Boolean like;
    private VideoDto playlist;
    private String videoId;
    @Builder
    public CollectionDto(Long collectionId, Boolean like, VideoDto playList, String videoId) {
        this.collectionId = collectionId;
        this.like = like;
        this.playlist = playList;
        this.videoId = videoId;
    }
}
