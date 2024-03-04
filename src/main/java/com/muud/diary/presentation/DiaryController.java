package com.muud.diary.presentation;

import com.muud.diary.application.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryPreviewResponse;
import com.muud.diary.dto.DiaryRequest;
import com.muud.diary.dto.DiaryResponse;
import com.muud.emotion.entity.Emotion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<Object> writeDiary(@Valid @RequestBody DiaryRequest diaryRequest) {

        Diary diary = diaryService.writeDiary(diaryRequest);
        return ResponseEntity.created(URI.create("/diaries/"+diary.getId())).build();
    }

    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiaryResponse(@PathVariable("diaryId") Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiaryResponse(diaryId));
    }

    @GetMapping("/diaries/month")
    public ResponseEntity<List<DiaryResponse>> getDiaryResponseListByYearMonth(@RequestParam(name = "date", required = true) String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ResponseEntity.ok(diaryService.getDiaryResponseListByYearMonth(yearMonth));
    }

    @PutMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> updatePost(@PathVariable("diaryId") Long diaryId,
                                                    @Valid @RequestBody DiaryRequest diaryRequest) {
        return ResponseEntity.ok(diaryService.updateContent(diaryId, diaryRequest));
    }

    @GetMapping("/diaries/emotion")
    public ResponseEntity<List<DiaryPreviewResponse>> getDiaryResponseListByEmotion(@RequestParam(name = "emotion", required = true) Emotion emotion) {
        return ResponseEntity.ok(diaryService.getDiaryResponseListByEmotion(emotion));
    }
}