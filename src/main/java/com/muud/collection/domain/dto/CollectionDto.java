package com.muud.collection.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muud.collection.domain.Collection;
import com.muud.playlist.domain.dto.VideoDto;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CollectionDto(Long collectionId,
                            Boolean like,
                            VideoDto playlist,
                            String videoId) {

    public static CollectionDto of(Long collectionId, Boolean like, String videoId) {
        return new CollectionDto(collectionId, like, null, videoId);
    }

    public static CollectionDto from(Collection collection) {
        return new CollectionDto(
                collection.getId(),
                collection.isLiked(),
                Optional.ofNullable(collection.getPlayList())
                        .map(playList -> playList.toDto())
                        .orElse(null),
                null);
    }

}
