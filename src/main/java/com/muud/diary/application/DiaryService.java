package com.muud.diary.application;

import com.muud.diary.domain.Diary;
import com.muud.diary.dto.DiaryPreviewResponse;
import com.muud.diary.dto.DiaryRequest;
import com.muud.diary.dto.DiaryResponse;
import com.muud.diary.repository.DiaryRepository;
import com.muud.emotion.entity.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
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

    public DiaryResponse getDiaryResponse(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);
        checkForbiddenUser(userId, diary);
        return DiaryResponse.from(diary);
    }

    public List<DiaryResponse> getDiaryResponseListByYearMonth(Long userId, YearMonth yearMonth) {
        List<Diary> diaryList = diaryRepository.findByMonthAndYear(userId, yearMonth.getMonthValue(), yearMonth.getYear());
        return diaryList.stream()
                .map(DiaryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryResponse updateContent(Long userId, Long diaryId, DiaryRequest diaryRequest) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);

        checkForbiddenUser(userId, diary);

        diary.updateContent(diaryRequest.content());
        
        Diary updatedDiary = diaryRepository.save(diary);
        return DiaryResponse.from(updatedDiary);
    }

    public List<Diary> getDiaryList(Long userId) {
        return diaryRepository.findByUserId(userId);
    }

    public List<DiaryPreviewResponse> getDiaryResponseListByEmotion(Long userId, Emotion emotion) {
        List<Diary> diaryList = diaryRepository.findByEmotion(userId, emotion);

        if (!diaryList.isEmpty()) {
            checkForbiddenUser(userId, diaryList.get(0));
        }

        return diaryList.stream()
                .map(DiaryPreviewResponse::from)
                .collect(Collectors.toList());
    }

    private void checkForbiddenUser(Long userId, Diary diary) {
        if (!isOwnerOfDiary(userId, diary)) {
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
    }

    private Boolean isOwnerOfDiary(Long userId, Diary diary) {
        return diary.getUser().getId().equals(userId);
    }
}