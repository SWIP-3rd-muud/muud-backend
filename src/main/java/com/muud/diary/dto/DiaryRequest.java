package com.muud.diary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DiaryRequest(
        @Size(max = 200, message = "내용은 최대 200 글자 입니다.")
        String content,
        @NotNull String emotionName,
        @NotNull LocalDate referenceDate,
        @NotNull Long playlistId) {
}