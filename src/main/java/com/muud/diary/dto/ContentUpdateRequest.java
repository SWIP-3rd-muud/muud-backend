package com.muud.diary.dto;

import jakarta.validation.constraints.Size;

public record ContentUpdateRequest(
        @Size(max = 200, message = "내용은 최대 200 글자 입니다.") String content) {
}