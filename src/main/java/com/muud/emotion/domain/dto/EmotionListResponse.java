package com.muud.emotion.domain.dto;

import java.util.List;

public record EmotionListResponse(List<EmotionValue> emotionList) {

    public static EmotionListResponse from(List<EmotionValue> emotionList) {
        return new EmotionListResponse(emotionList);
    }
}
