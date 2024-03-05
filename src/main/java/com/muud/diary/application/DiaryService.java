package com.muud.diary.application;

import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryPreviewResponse;
import com.muud.diary.dto.DiaryRequest;
import com.muud.diary.dto.DiaryResponse;
import com.muud.diary.repository.DiaryRepository;
import com.muud.emotion.entity.Emotion;
import com.muud.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Transactional
    public Diary writeDiary(User user, DiaryRequest diaryRequest) {
        return diaryRepository.save(
                new Diary(diaryRequest.content(),
                        Emotion.valueOf(diaryRequest.emotionName().toUpperCase()),
                        user));
    }

    public DiaryResponse getDiaryResponse(Long diaryId) {
        return DiaryResponse.from(diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new));
    }

    public List<DiaryResponse> getDiaryResponseListByYearMonth(YearMonth yearMonth) {
        List<Diary> diaryList = diaryRepository.findByMonthAndYear(yearMonth.getMonthValue(), yearMonth.getYear());
        return diaryList.stream()
                .map(DiaryResponse::from)
                .collect(Collectors.toList());
    }

    public DiaryResponse updateContent(Long diaryId, DiaryRequest diaryRequest) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);
        diary.updateContent(diaryRequest.content());
        
        Diary updatedDiary = diaryRepository.save(diary);
        return DiaryResponse.from(updatedDiary);
    }

    public List<Diary> getDiaryList() {
        return diaryRepository.findAll();
    }

    public List<DiaryPreviewResponse> getDiaryResponseListByEmotion(Emotion emotion) {
        List<Diary> diaryList = diaryRepository.findByEmotion(emotion);
        return diaryList.stream()
                .map(DiaryPreviewResponse::from)
                .collect(Collectors.toList());
    }
}