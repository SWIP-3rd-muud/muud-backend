package com.muud.emotion.controller.v1;

import com.muud.auth.jwt.Auth;
import com.muud.emotion.domain.dto.EmotionListResponse;
import com.muud.emotion.domain.dto.TagListResponse;
import com.muud.emotion.service.EmotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "감정 API", description = "일기 작성에 필요한 감정 값들을 호출하는 API")
@RequestMapping("/api/v1/emotions")
public class EmotionControllerV1 {

    private final EmotionService emotionService;

    @Auth
    @Operation(summary = "감정 카드 호출", description = "감정 카드의 값들을 불러온다")
    @GetMapping
    public ResponseEntity<EmotionListResponse> getEmotionList() {
        return ResponseEntity.ok(emotionService.getEmotionValueList());
    }

    @Auth
    @Operation(summary = "감정 태그 호출", description = "감정 태그들을 불러온다")
    @GetMapping("/tags")
    public ResponseEntity<TagListResponse> getEmotionTagList() {
        return ResponseEntity.ok(emotionService.getEmotionTagList());
    }
}
