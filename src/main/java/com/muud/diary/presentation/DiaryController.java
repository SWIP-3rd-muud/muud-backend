package com.muud.diary.presentation;

import com.muud.diary.application.DiaryService;
import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ResponseEntity<Object> writeDiary(@RequestBody DiaryRequest diaryRequest) {

        Diary diary = diaryService.writeDiary(diaryRequest);
        return ResponseEntity.created(URI.create("/diaries/"+diary.getDiaryId())).build();
    }
}