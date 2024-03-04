package com.muud.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muud.playlist.dto.VideoDto;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionDto {
    private Long collectionId;
    private String videoId;
    private Boolean like;
    private VideoDto playlist;

    @Builder
    public CollectionDto(Long collectionId, String videoId, Boolean like) {
        this.collectionId = collectionId;
        this.videoId = videoId;
        this.like = like;
    }
}
