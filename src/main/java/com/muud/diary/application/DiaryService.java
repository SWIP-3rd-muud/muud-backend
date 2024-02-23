package com.muud.diary.application;

import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryRequest;
import com.muud.diary.dto.DiaryResponse;
import com.muud.diary.repository.DiaryRepository;
import com.muud.emotion.entity.Emotion;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Transactional
    public Diary writeDiary(DiaryRequest diaryRequest) {
        return diaryRepository.save(
                new Diary(diaryRequest.content(),
                        Emotion.valueOf(diaryRequest.emotionName().toUpperCase())));
    }

    public DiaryResponse getDiaryResponse(Long diaryId) {
        return DiaryResponse.from(diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new));
    }
}