package com.muud.diary.dto;

import com.muud.diary.domain.Diary;

public record DiaryPreviewResponse(Long diaryId,
//                                   PlayList playList,
                                   String content) {
    public static DiaryPreviewResponse from(Diary diary) {
        return new DiaryPreviewResponse(
                diary.getId(),
                diary.getContent());
    }
}