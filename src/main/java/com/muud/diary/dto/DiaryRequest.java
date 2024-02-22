package com.muud.diary.dto;

public record DiaryRequest(
        String content,
        String emotionName) {
}