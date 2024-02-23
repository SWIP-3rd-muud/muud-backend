package com.muud.emotion.controller;

import com.muud.emotion.dto.EmotionResponse;
import com.muud.emotion.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @GetMapping("/emotions")
    public ResponseEntity<List<EmotionResponse>> getEmotionResponseList() {

        List<EmotionResponse> emotionResponseList = emotionService.getEmotionResponseList();
        return ResponseEntity.ok(emotionResponseList);
    }

    @GetMapping("/emotions/{emotionName}")
    public ResponseEntity<EmotionResponse> getEmotionResponse(@PathVariable("emotionName") String emotionName) {

        EmotionResponse emotionResponse = emotionService.getEmotionResponse(emotionName);
        return ResponseEntity.ok(emotionResponse);
    }
}
