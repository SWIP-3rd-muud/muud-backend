package com.muud.emotion.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum Emotion {
    EMOTION1("기쁨", "쨍쨍한 햇빛", "날씨 좋은 날,  푸른 하늘에 보이는 쨍쨍한 햇빛 같아요", "\\uD83D\\uDE00", new String[]{"기쁨", "설렘", "행복"}),
    EMOTION2("우울", "우르르 쾅쾅", "마음을 쾅쾅 두드리는 화가 가득해요", "\\uD83D\\uDE00", new String[]{"분노", "짜증", "극대노"}),
    EMOTION3("슬픔", "호우주의보", "거센 비가 마음을 적시고 있어요", "\\uD83D\\uDE00", new String[]{"눈물나는", "후회", "슬픔"}),
    EMOTION4("피곤", "안개주의보", "온통 회색빛인 마음에 안개가 뒤덮였어요", "\\uD83D\\uDE00", new String[]{"피곤한", "지침", "기운없음"}),
    EMOTION5("덤덤", "잔잔한 구름", "바람 따라 흘러가는 구름처럼 조용해요", "\\uD83D\\uDE00", new String[]{"그저 그럼", "SOSO", "덤덤"}),
    EMOTION6("분노", "태풍의 눈", "스트레스로 어질어질 빙글빙글 돌아요", "\\uD83D\\uDE00", new String[]{"스트레스", "우울", "숨막혀요"});

    private final String titleEmotion;
    private final String combinedName;
    private final String description;
    private final String emoji;
    private final String[] tags;

    Emotion(String titleEmotion, String combinedName, String description, String emoji, String[] tags) {
        this.titleEmotion = titleEmotion;
        this.combinedName = combinedName;
        this.description = description;
        this.emoji = emoji;
        this.tags = tags;
    }
}
