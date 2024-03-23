package com.muud.diary.domain.dto;

import com.muud.diary.domain.Diary;
import com.muud.emotion.domain.Emotion;
import com.muud.playlist.domain.dto.VideoDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DiaryResponse(Long id,
                            String content,
                            Emotion emotion,
                            LocalDateTime createdDate,
                            LocalDateTime updatedDate,
                            LocalDate referenceDate,
                            String imageUrl,
                            VideoDto playlist) {
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getCreatedDate(),
                diary.getUpdatedDate(),
                diary.getReferenceDate(),
                diary.getImageUrl(),
                diary.getPlayList().toDto());
    }
}
