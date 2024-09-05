package com.muud.emotion.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmotionCard {

    JOY("joy", "기분 좋음", "쨍쨍한 햇빛", "/images/joy-mood.png"),
    GENERAL("general", "보통", "잔잔한 구름", "/images/general-mood.png"),
    ANGRY("angry", "화남", "우르르 쾅쾅 천둥번개", "/images/angry-mood.png"),
    SADNESS("sadness", "슬픔", "주륵주륵 소나기", "/images/sadness-mood.png"),
    TIRED("tired", "지침", "뭉게뭉게 먹구름", "/images/tired-mood.png");

    private final String title;
    private final String subTitle;
    private final String combinedName;
    private final String path;
}
