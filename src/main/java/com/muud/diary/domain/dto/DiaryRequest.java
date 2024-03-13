package com.muud.diary.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DiaryRequest(
        @Size(max = 300, message = "내용은 최대 300 글자 입니다.")
        String content,
        @NotNull String emotionName,
        @NotNull LocalDate referenceDate,
        @NotNull Long playlistId) {
}