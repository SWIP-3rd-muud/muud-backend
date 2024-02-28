package com.muud.emotion.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum Emotion {
    JOY("기쁨", "쨍쨍한 햇빛", "반짝반짝, 햇빛 샤워", "/images/sunny-mood.png",
            new String[]{"기쁨", "설렘", "행복"}, "두근두근 기분이 좋고, 웃을 일이 많아요"),
    ANGER("분노", "우르르 쾅쾅", "화가 마음을 쾅쾅 두드려요", "/images/thunder-mood.png",
            new String[]{"분노", "짜증", "극대노"}, "왠지 모르게 작은 일에도 쉽게 화가 나요"),
    SAD("슬픔", "호우주의보", "거센 비가 마음을 적셔요", "/images/rainy-mood.png",
            new String[]{"눈물나는", "후회", "슬픔"}, "뭔가 입맛이 없고, 자꾸 슬퍼져요"),
    TIRED("피곤", "안개주의보", "안개가 뒤덮인 회색빛 마음", "/images/foggy-mood.png",
            new String[]{"피곤함", "지침", "기운없음"}, "할 일이 너무 많고, 하루가 길게 느껴져요"),
    CALM("덤덤", "잔잔한 구름", "마음 위에 구름이 둥둥", "/images/cloudy-mood.png",
            new String[]{"그저 그럼", "SOSO", "덤덤"}, "내 마음은 조용하고 단순한 분위기예요"),
    BLUE("우울", "태풍의 눈", "마음이 어질어질 빙글빙글", "/images/typhoon-mood.png",
            new String[]{"스트레스", "우울", "숨막혀요"}, "답답하고, 스트레스를 많이 받았어요");

    private final String titleEmotion;
    private final String combinedName;
    private final String description;
    private final String emoji;
    private final String[] tags;
    private final String question;

    Emotion(String titleEmotion, String combinedName, String description, String emoji, String[] tags, String question) {
        this.titleEmotion = titleEmotion;
        this.combinedName = combinedName;
        this.description = description;
        this.emoji = emoji;
        this.tags = tags;
        this.question = question;
    }
}
