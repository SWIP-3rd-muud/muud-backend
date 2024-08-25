package com.muud.emotion.domain.dto;

import java.util.List;

public record TagListResponse(List<String> tagList) {

    public static TagListResponse from(List<String> tagList) {
        return new TagListResponse(tagList);
    }
}
