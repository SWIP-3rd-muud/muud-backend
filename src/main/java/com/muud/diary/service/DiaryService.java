package com.muud.diary.service;

import com.muud.diary.domain.Diary;
import com.muud.diary.domain.dto.ContentUpdateRequest;
import com.muud.diary.domain.dto.DiaryPreviewResponse;
import com.muud.diary.domain.dto.DiaryRequest;
import com.muud.diary.domain.dto.DiaryResponse;
import com.muud.diary.repository.DiaryRepository;
import com.muud.emotion.domain.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.global.util.PhotoManager;
import com.muud.playlist.service.PlayListService;
import com.muud.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final PhotoManager photoManager;
    private final PlayListService playListService;

    @Value("${cloud.aws.s3.image-directory}")
    private String imageDirectory;

    @Transactional
    public Diary writeDiary(User user, DiaryRequest diaryRequest, MultipartFile image) {
        checkWritable(user, diaryRequest);
        return diaryRepository.save(
                new Diary(diaryRequest.content(),
                        Emotion.valueOf(diaryRequest.emotionName().toUpperCase()),
                        user,
                        diaryRequest.referenceDate(),
                        saveImage(image),
                        playListService.getPlayList(diaryRequest.playlistId())));
    }

    private String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return photoManager.upload(image, imageDirectory);
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
    public DiaryResponse updateContent(Long userId, Long diaryId, ContentUpdateRequest contentUpdateRequest) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);
        checkForbiddenUser(userId, diary);
        diary.updateContent(contentUpdateRequest.content());
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

    private void checkWritable(User user, DiaryRequest diaryRequest) {
        int count = diaryRepository.countDiariesByUserIdAndReferenceDate(user.getId(), diaryRequest.referenceDate());
        if (count > 0) {
            throw new ApiException(ExceptionType.BAD_REQUEST);
        }
    }
}