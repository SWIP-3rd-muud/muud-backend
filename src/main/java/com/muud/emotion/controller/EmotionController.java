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
    public ResponseEntity<List<EmotionResponse>> getEmotionResponseList() {

        List<EmotionResponse> emotionResponseList = emotionService.getEmotionResponseList();
        return ResponseEntity.ok(emotionResponseList);
    }

    @Auth
    @GetMapping("/emotions/{emotionName}")
    public ResponseEntity<EmotionResponse> getEmotionResponse(@PathVariable("emotionName") final String emotionName) {

        EmotionResponse emotionResponse = emotionService.getEmotionResponse(emotionName);
        return ResponseEntity.ok(emotionResponse);
    }

    @Auth
    @GetMapping("/emotions/ranking")
    public ResponseEntity<List<EmotionCountResponse>> getEmotionCount(@RequestAttribute("user") final User user,
                                                                      @RequestParam(name = "ascending", required = true) final boolean ascending) {
        List<Diary> diaryList = diaryService.getDiaryList(user.getId());
        List<EmotionCountResponse> emotionCountResponse = emotionService.getEmotionCount(diaryList, ascending);
        return ResponseEntity.ok(emotionCountResponse);
    }

    @Auth
    @GetMapping("/emotions/questions")
    public ResponseEntity<List<QuestionResponse>> getAllQuestionResponse() {
        List<QuestionResponse> questionResponseList = emotionService.getAllQuestionResponse();
        return ResponseEntity.ok(questionResponseList);
    }
}