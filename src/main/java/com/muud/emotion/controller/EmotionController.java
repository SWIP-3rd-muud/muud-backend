package com.muud.emotion.controller;

import com.muud.auth.jwt.Auth;
import com.muud.diary.service.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.emotion.domain.dto.EmotionCountResponse;
import com.muud.emotion.domain.dto.EmotionResponse;
import com.muud.emotion.domain.dto.QuestionResponse;
import com.muud.emotion.service.EmotionService;
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

    @Auth
    @GetMapping("/emotions")
    public ResponseEntity<List<EmotionResponse>> getEmotionList() {
        List<EmotionResponse> emotionList = emotionService.getEmotionList();
        return ResponseEntity.ok(emotionList);
    }

    @Auth
    @GetMapping("/emotions/{emotionName}")
    public ResponseEntity<EmotionResponse> getEmotionByName(@PathVariable("emotionName") final String emotionName) {
        EmotionResponse emotion = emotionService.getEmotionByName(emotionName);
        return ResponseEntity.ok(emotion);
    }

    @Auth
    @GetMapping("/emotions/ranking")
    public ResponseEntity<List<EmotionCountResponse>> getEmotionAndCount(@RequestAttribute("user") final User user,
                                                                         @RequestParam(name = "ascending", required = true) final boolean ascending) {
        List<Diary> diaryList = diaryService.getDiaryList(user.getId());
        List<EmotionCountResponse> emotionCount = emotionService.getEmotionAndCount(diaryList, ascending);
        return ResponseEntity.ok(emotionCount);
    }

    @Auth
    @GetMapping("/emotions/questions")
    public ResponseEntity<List<QuestionResponse>> getAllQuestionList() {
        List<QuestionResponse> questionList = emotionService.getAllQuestionList();
        return ResponseEntity.ok(questionList);
    }
}