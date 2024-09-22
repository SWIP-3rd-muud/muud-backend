package com.muud.emotion.controller;

import com.muud.diary.service.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.emotion.domain.dto.EmotionCountResponse;
import com.muud.emotion.domain.dto.EmotionResponse;
import com.muud.emotion.domain.dto.QuestionResponse;
import com.muud.emotion.service.EmotionService;
import com.muud.global.util.SecurityUtils;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;
    private final DiaryService diaryService;

    @GetMapping("/emotions")
    public ResponseEntity<List<EmotionResponse>> getEmotionList() {
        List<EmotionResponse> emotionList = emotionService.getEmotionList();
        return ResponseEntity.ok(emotionList);
    }

    @GetMapping("/emotions/{emotionName}")
    public ResponseEntity<EmotionResponse> getEmotionByName(@PathVariable("emotionName") final String emotionName) {
        EmotionResponse emotion = emotionService.getEmotionByName(emotionName);
        return ResponseEntity.ok(emotion);
    }

    @GetMapping("/emotions/ranking")
    public ResponseEntity<List<EmotionCountResponse>> getEmotionAndCount(
            @RequestParam(name = "ascending", required = true) final boolean ascending) {
        User user = SecurityUtils.getCurrentUser();
        List<Diary> diaryList = diaryService.getDiaryList(user);
        List<EmotionCountResponse> emotionCount = emotionService.getEmotionAndCount(diaryList, ascending);
        return ResponseEntity.ok(emotionCount);
    }

    @GetMapping("/emotions/questions")
    public ResponseEntity<List<QuestionResponse>> getAllQuestionList() {
        List<QuestionResponse> questionList = emotionService.getAllQuestionList();
        return ResponseEntity.ok(questionList);
    }
}