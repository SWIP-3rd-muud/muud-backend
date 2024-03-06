package com.muud.diary.dto;

import com.muud.diary.domain.Diary;
import com.muud.emotion.entity.Emotion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DiaryResponse(Long id,
                            String content,
                            Emotion emotion,
                            LocalDateTime createdDate,
                            LocalDateTime updatedDate,
                            LocalDate referenceDate,
                            String imageUrl) {
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getCreatedDate(),
                diary.getUpdatedDate(),
                diary.getReferenceDate(),
                diary.getImageUrl());
    }
}
