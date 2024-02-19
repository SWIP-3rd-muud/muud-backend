package com.muud.emotion.entity;

import lombok.Getter;

@Getter
public enum Emotion {
    EMOTION1(new String[]{"후회", "슬픔"}, "호우주의보", "조용하고 잔잔한 봄날의 그늘", "\uD83D\uDE0A", new String[]{"조용", "잔잔", "우울"}),
    EMOTION2(new String[]{"덤덤"}, "덤덤하네", "덤덤합니다", "\\uD83D\\uDE00", new String[]{"그저그럼", "SOSO", "덤덤"});

    private final String[] titles;
    private final String combinedName;
    private final String description;
    private final String emoji;
    private final String[] tags;

    Emotion(String[] titles, String combinedName, String description, String emoji, String[] tags) {
        this.titles = titles;
        this.combinedName = combinedName;
        this.description = description;
        this.emoji = emoji;
        this.tags = tags;
    }
}
