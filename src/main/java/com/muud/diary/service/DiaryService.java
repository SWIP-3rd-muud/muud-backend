package com.muud.diary.service;

import com.muud.diary.config.ImageDirectoryConfig;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ImageDirectoryConfig imageDirectoryConfig;

    @Transactional
    public Diary writeDiary(final User user,
                            final DiaryRequest diaryRequest,
                            final MultipartFile image) {
        checkWritable(user, diaryRequest);
        return diaryRepository.save(Diary.of(user, diaryRequest, saveImage(image), playListService.getPlayList(diaryRequest.playlistId())));
    }

    private String saveImage(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return photoManager.upload(image, imageDirectoryConfig.getImageDirectory());
    }

    @Transactional(readOnly = true)
    public DiaryResponse getDiary(final User user,
                                  final Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(IllegalArgumentException::new);
        checkAuthorized(user, diary);
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
                .orElseThrow(IllegalArgumentException::new);
        checkAuthorized(user, diary);
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
            checkAuthorized(user, diaryList.get(0));
        }
        return diaryList.stream()
                .map(DiaryPreviewResponse::from)
                .collect(Collectors.toList());
    }

    private void checkAuthorized(final User user,
                                 final Diary diary) {
        if (!isOwnerOfDiary(user, diary)) {
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
    }

    private Boolean isOwnerOfDiary(final User user,
                                   final Diary diary) {
        return diary.getUser().getId().equals(user.getId());
    }

    private void checkWritable(final User user,
                               final DiaryRequest diaryRequest) {
        int count = diaryRepository.countDiaryOnDate(user.getId(), diaryRequest.referenceDate());
        if (count > 0) {
            throw new ApiException(ExceptionType.BAD_REQUEST, "일기는 하루에 한개만 작성 가능합니다.");
        }
    }
}