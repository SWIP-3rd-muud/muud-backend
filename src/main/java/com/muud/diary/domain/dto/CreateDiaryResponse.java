package com.muud.diary.domain.dto;

import com.muud.diary.domain.Diary;

public record CreateDiaryResponse(Long DiaryId) {
    public static CreateDiaryResponse from(Diary diary) {
        return new CreateDiaryResponse(
                diary.getId());
    }
}