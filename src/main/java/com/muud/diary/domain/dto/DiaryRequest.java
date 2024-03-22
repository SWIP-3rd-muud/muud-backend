package com.muud.diary.domain.dto;

import com.muud.emotion.domain.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DiaryRequest(

        @Size(max = 300, message = "내용은 최대 300 글자 입니다.")
        String content,

        @NotNull(message = "감정을 입력해주세요.")
        String emotionName,

        @NotNull(message = "기준일을 입력해주세요.")
        LocalDate referenceDate,

        @Min(value = 1L, message = "플레이리스트 ID가 1보다 작습니다")
        Long playlistId) {

        public DiaryRequest {
                validateEmotionName(emotionName);
        }

        private void validateEmotionName(String emotionName) {
                if (!Emotion.isValidEmotionName(emotionName)) {
                        throw new ApiException(ExceptionType.BAD_REQUEST, "유효하지 않은 감정입니다.");
                }
        }
}