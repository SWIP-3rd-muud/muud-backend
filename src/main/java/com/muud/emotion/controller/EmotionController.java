package com.muud.emotion.controller;

import com.muud.diary.application.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.emotion.dto.EmotionCountResponse;
import com.muud.emotion.dto.EmotionResponse;
import com.muud.emotion.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;
    private final DiaryService diaryService;

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

    @GetMapping("/emotions/ranking")
    public ResponseEntity<List<EmotionCountResponse>> getEmotionCount(@RequestParam(name = "ascending", required = true) boolean ascending) {
        List<Diary> diaryList = diaryService.getDiaryList();
        List<EmotionCountResponse> emotionCountResponse = emotionService.getEmotionCount(diaryList, ascending);
        return ResponseEntity.ok(emotionCountResponse);
    }
}
