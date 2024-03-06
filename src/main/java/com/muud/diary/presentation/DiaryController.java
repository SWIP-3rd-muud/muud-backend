package com.muud.diary.presentation;

import com.muud.auth.jwt.Auth;
import com.muud.diary.application.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryPreviewResponse;
import com.muud.diary.dto.DiaryRequest;
import com.muud.diary.dto.DiaryResponse;
import com.muud.emotion.entity.Emotion;
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
    public ResponseEntity<Long> writeDiary(@RequestAttribute("user") User user,
                                           @ModelAttribute DiaryRequest diaryRequest,
                                           @RequestPart(name = "multipartFile", required = false) MultipartFile multipartFile) {
        Diary diary = diaryService.writeDiary(user, diaryRequest, multipartFile);
        return ResponseEntity.created(URI.create("/diaries/"+diary.getId()))
                .body(diary.getId());
    }

    @Auth
    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiaryResponse(@RequestAttribute("user") User user,
                                                          @PathVariable("diaryId") Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiaryResponse(user.getId(), diaryId));
    }

    @Auth
    @GetMapping("/diaries/month")
    public ResponseEntity<List<DiaryResponse>> getDiaryResponseListByYearMonth(@RequestAttribute("user") User user,
                                                                               @RequestParam(name = "date", required = true) String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ResponseEntity.ok(diaryService.getDiaryResponseListByYearMonth(user.getId(), yearMonth));
    }

    @Auth
    @PutMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> updatePost(@RequestAttribute("user") User user,
                                                    @PathVariable("diaryId") Long diaryId,
                                                    @Valid @RequestBody DiaryRequest diaryRequest) {
        return ResponseEntity.ok(diaryService.updateContent(user.getId(), diaryId, diaryRequest));
    }

    @Auth
    @GetMapping("/diaries/emotion")
    public ResponseEntity<List<DiaryPreviewResponse>> getDiaryResponseListByEmotion(@RequestAttribute("user") User user,
                                                                                    @RequestParam(name = "emotion", required = true) Emotion emotion) {
        return ResponseEntity.ok(diaryService.getDiaryResponseListByEmotion(user.getId(), emotion));
    }
}