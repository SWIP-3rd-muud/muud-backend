package com.muud.diary.presentation;

import com.muud.diary.application.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryRequest;
import com.muud.diary.dto.DiaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<Object> writeDiary(@Valid @RequestBody DiaryRequest diaryRequest) {

        Diary diary = diaryService.writeDiary(diaryRequest);
        return ResponseEntity.created(URI.create("/diaries/"+diary.getDiaryId())).build();
    }

    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiaryResponse(@PathVariable("diaryId") Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiaryResponse(diaryId));
    }
}