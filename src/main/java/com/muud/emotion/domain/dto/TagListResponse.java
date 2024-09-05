package com.muud.emotion.domain.dto;

import java.util.List;

public record TagListResponse(List<String> tagList, String focusedTag) {

    public static TagListResponse from(List<String> tagList, String focusedTag) {
        return new TagListResponse(tagList, focusedTag);
    }
}
