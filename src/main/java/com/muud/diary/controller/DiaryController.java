package com.muud.diary.controller;

import com.muud.auth.jwt.Auth;
import com.muud.diary.service.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.diary.domain.dto.ContentUpdateRequest;
import com.muud.diary.domain.dto.DiaryPreviewResponse;
import com.muud.diary.domain.dto.DiaryRequest;
import com.muud.diary.domain.dto.DiaryResponse;
import com.muud.emotion.domain.Emotion;
import com.muud.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @Auth
    @PostMapping("/diaries")
    public ResponseEntity<Long> writeDiary(@RequestAttribute("user") final User user,
                                           @Valid @ModelAttribute final DiaryRequest diaryRequest,
                                           @RequestPart(name = "multipartFile", required = false) final MultipartFile multipartFile) {
        Diary diary = diaryService.writeDiary(user, diaryRequest, multipartFile);
        return ResponseEntity.created(URI.create("/diaries/"+diary.getId()))
                .body(diary.getId());
    }

    @Auth
    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiary(@RequestAttribute("user") final User user,
                                                  @PathVariable("diaryId") final Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(user.getId(), diaryId));
    }

    @Auth
    @GetMapping("/diaries/month")
    public ResponseEntity<List<DiaryResponse>> getMonthlyDiaryList(@RequestAttribute("user") final User user,
                                                                   @RequestParam(name = "date", required = true) final String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ResponseEntity.ok(diaryService.getMonthlyDiaryList(user.getId(), yearMonth));
    }

    @Auth
    @PutMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> updateContent(@RequestAttribute("user") final User user,
                                                       @PathVariable("diaryId") final Long diaryId,
                                                       @Valid @RequestBody final ContentUpdateRequest contentUpdateRequest) {
        return ResponseEntity.ok(diaryService.updateContent(user.getId(), diaryId, contentUpdateRequest));
    }

    @Auth
    @GetMapping("/diaries/emotion")
    public ResponseEntity<List<DiaryPreviewResponse>> getDiaryPreviewListByEmotion(@RequestAttribute("user") final User user,
                                                                                   @RequestParam(name = "emotion", required = true) final Emotion emotion) {
        return ResponseEntity.ok(diaryService.getDiaryPreviewListByEmotion(user.getId(), emotion));
    }
}