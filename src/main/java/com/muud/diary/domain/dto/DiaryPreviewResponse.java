package com.muud.diary.domain.dto;

import com.muud.diary.domain.Diary;

public record DiaryPreviewResponse(Long diaryId,
                                   String content) {
    public static DiaryPreviewResponse from(Diary diary) {
        return new DiaryPreviewResponse(
                diary.getId(),
                diary.getContent());
    }
}