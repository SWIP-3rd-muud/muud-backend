package com.muud.diary.service;

import com.muud.diary.domain.Diary;
import com.muud.diary.domain.dto.*;
import com.muud.diary.repository.DiaryRepository;
import com.muud.emotion.domain.Emotion;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static com.muud.diary.exception.DiaryErrorCode.*;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public CreateDiaryResponse create(final User user,
                                     final DiaryRequest diaryRequest,
                                     final String image,
                                     final PlayList playList) {
        checkWritable(user, diaryRequest);
        Diary diary = diaryRepository.save(Diary.of(user, diaryRequest, image, playList));
        return CreateDiaryResponse.from(diary);
    }

    @Transactional(readOnly = true)
    public DiaryResponse getDiary(final User user,
                                  final Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DIARY_NOT_FOUND::defaultException);
        validateDiaryOwnership(user, diary);
        return DiaryResponse.from(diary);
    }

    @Transactional(readOnly = true)
    public List<DiaryResponse> getMonthlyDiaryList(final User user,
                                                   final YearMonth yearMonth) {
        return diaryRepository.findByMonthAndYear(user.getId(), yearMonth.getMonthValue(), yearMonth.getYear())
                .stream()
                .map(DiaryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryResponse updateContent(final User user,
                                       final Long diaryId,
                                       final ContentUpdateRequest contentUpdateRequest) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(DIARY_NOT_FOUND::defaultException);
        validateDiaryOwnership(user, diary);
        diary.updateContent(contentUpdateRequest.content());
        Diary updatedDiary = diaryRepository.save(diary);
        return DiaryResponse.from(updatedDiary);
    }

    @Transactional(readOnly = true)
    public List<Diary> getDiaryList(final User user) {
        return diaryRepository.findByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public List<DiaryPreviewResponse> getDiaryPreviewListByEmotion(final User user,
                                                                   final Emotion emotion) {
        List<Diary> diaryList = diaryRepository.findByEmotion(user.getId(), emotion);

        if (!diaryList.isEmpty()) {
            validateDiaryOwnership(user, diaryList.get(0));
        }
        return diaryList.stream()
                .map(DiaryPreviewResponse::from)
                .collect(Collectors.toList());
    }

    private void validateDiaryOwnership(final User user,
                                        final Diary diary) {
        if (!user.checkValidId(diary.getUser().getId())) {
            throw DIARY_ACCESS_DENIED.defaultException();
        }
    }

    private void checkWritable(final User user,
                               final DiaryRequest diaryRequest) {
        int count = diaryRepository.countDiaryOnDate(user.getId(), diaryRequest.referenceDate());
        if (count > 0) {
            throw DIARY_ALREADY_EXISTS.defaultException();
        }
    }
}