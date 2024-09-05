package com.muud.emotion.domain.dto;

import com.muud.emotion.domain.EmotionCard;

public record EmotionValue(
        String title,
        String subTitle,
        String combinedName,
        String path) {

    public static EmotionValue from(EmotionCard emotionCard) {
        return new EmotionValue(
                emotionCard.getTitle(),
                emotionCard.getSubTitle(),
                emotionCard.getCombinedName(),
                emotionCard.getPath());
    }
}
