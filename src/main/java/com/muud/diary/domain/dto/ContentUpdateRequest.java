package com.muud.diary.domain.dto;

import jakarta.validation.constraints.Size;

public record ContentUpdateRequest(
        @Size(max = 300, message = "내용은 최대 300 글자 입니다.") String content) {
}