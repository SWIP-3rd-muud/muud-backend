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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final PhotoManager photoManager;
    private final PlayListService playListService;

    @Value("${cloud.aws.s3.image-directory}")
    private String imageDirectory;

    @Transactional
    public Diary writeDiary(final User user,
                            final DiaryRequest diaryRequest,
                            final MultipartFile image) {
        checkWritable(user, diaryRequest);
        return diaryRepository.save(
                new Diary(diaryRequest.content(),
                        Emotion.valueOf(diaryRequest.emotionName().toUpperCase()),
                        user,
                        diaryRequest.referenceDate(),
                        saveImage(image),
                        playListService.getPlayList(diaryRequest.playlistId())));
    }

    private String saveImage(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return photoManager.upload(image, imageDirectory);
    }

    public DiaryResponse getDiary(final Long userId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);
        checkAuthorized(userId, diary);
        return DiaryResponse.from(diary);
    }

    public List<DiaryResponse> getMonthlyDiaryList(final Long userId,
                                                   final YearMonth yearMonth) {
        return diaryRepository.findByMonthAndYear(userId, yearMonth.getMonthValue(), yearMonth.getYear())
                .stream()
                .map(DiaryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryResponse updateContent(final Long userId,
                                       final Long diaryId,
                                       final ContentUpdateRequest contentUpdateRequest) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);
        checkAuthorized(userId, diary);
        diary.updateContent(contentUpdateRequest.content());
        Diary updatedDiary = diaryRepository.save(diary);
        return DiaryResponse.from(updatedDiary);
    }

    public List<Diary> getDiaryList(final Long userId) {
        return diaryRepository.findByUserId(userId);
    }

    public List<DiaryPreviewResponse> getDiaryPreviewListByEmotion(final Long userId,
                                                                   final Emotion emotion) {
        List<Diary> diaryList = diaryRepository.findByEmotion(userId, emotion);

        if (!diaryList.isEmpty()) {
            checkAuthorized(userId, diaryList.get(0));
        }
        return diaryList.stream()
                .map(DiaryPreviewResponse::from)
                .collect(Collectors.toList());
    }

    private void checkAuthorized(final Long userId,
                                 final Diary diary) {
        if (!isOwnerOfDiary(userId, diary)) {
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
    }

    private Boolean isOwnerOfDiary(final Long userId,
                                   final Diary diary) {
        return diary.getUser().getId().equals(userId);
    }

    private void checkWritable(final User user,
                               final DiaryRequest diaryRequest) {
        int count = diaryRepository.countDiaryOnDate(user.getId(), diaryRequest.referenceDate());
        if (count > 0) {
            throw new ApiException(ExceptionType.BAD_REQUEST, "일기는 하루에 한개만 작성 가능합니다.");
        }
    }
}