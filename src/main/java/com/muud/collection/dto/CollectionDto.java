package com.muud.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionDto {
    private Long collectionId;
    private String videoId;
    private Boolean like;

    @Builder
    public CollectionDto(Long collectionId, String videoId, Boolean like) {
        this.collectionId = collectionId;
        this.videoId = videoId;
        this.like = like;
    }
}
