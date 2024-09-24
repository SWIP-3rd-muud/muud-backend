package com.muud.diary.service;

import com.muud.diary.domain.Diary;
import com.muud.diary.domain.dto.ContentUpdateRequest;
import com.muud.diary.domain.dto.CreateDiaryResponse;
import com.muud.diary.domain.dto.DiaryRequest;
import com.muud.diary.domain.dto.DiaryResponse;
import com.muud.diary.exception.DiaryErrorCode;
import com.muud.diary.exception.DiaryException;
import com.muud.diary.repository.DiaryRepository;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @InjectMocks
    private DiaryService diaryService;

    private User userA;
    private User userB;
    private DiaryRequest diaryRequest;
    private DiaryRequest diaryRequest_tomorrow;
    private DiaryRequest diaryRequest_yesterday;
    private PlayList playList;
    private Diary diary1;
    private Diary diary2;
    private Diary otherDiary;
    private ContentUpdateRequest contentUpdateRequest;

    @BeforeEach
    void setUp() {
        userA = User.builder()
                .email("user1@email.com")
                .build();
        ReflectionTestUtils.setField(userA, "id", 1L);

        userB = User.builder()
                .email("user2@email.com")
                .build();
        ReflectionTestUtils.setField(userB, "id", 2L);

        playList = PlayList.builder()
                .videoId("videoId1")
                .build();
        ReflectionTestUtils.setField(playList, "id", 1L);

        diaryRequest = new DiaryRequest("diary1", "JOY",
                LocalDate.of(2024, 9, 20), 1L);
        diaryRequest_tomorrow = new DiaryRequest("diary2", "JOY",
                LocalDate.of(2024, 9, 21), 1L);
        diaryRequest_yesterday = new DiaryRequest("content", "JOY",
                LocalDate.of(2024, 9, 19), 1L);

        diary1 = Diary.of(userA, diaryRequest, null, playList);
        ReflectionTestUtils.setField(diary1, "id", 1L);

        diary2 = Diary.of(userA, diaryRequest_tomorrow, null, playList);
        ReflectionTestUtils.setField(diary2, "id", 2L);

        otherDiary = Diary.of(userB, diaryRequest_yesterday, null, playList);
        ReflectionTestUtils.setField(otherDiary, "id", 3L);

        contentUpdateRequest = new ContentUpdateRequest("수정된 내용");
    }

    @Test
    @DisplayName("일기 작성 성공")
    void create_success() {
        // Given
        when(diaryRepository.save(any())).thenReturn(diary1);

        // When
        CreateDiaryResponse response = diaryService.create(userA, diaryRequest, null, playList);

        // Then
        assertNotNull(response);
        verify(diaryRepository, times(1)).save(any(Diary.class));
        assertEquals(diary1.getId(), response.DiaryId());
    }

    @Test
    @DisplayName("일기 작성 실패 - 해당 날짜에 이미 작성된 일기 존재")
    void create_failure_alreadyWrittenDiary() {
        // Given
        when(diaryRepository.countDiaryOnDate(anyLong(), any(LocalDate.class))).thenReturn(1);

        // When & Then
        DiaryException exception = assertThrows(DiaryException.class, () -> {
            diaryService.create(userA, diaryRequest, null, playList);
        });

        assertEquals(DiaryErrorCode.DIARY_ALREADY_EXISTS, exception.getErrorCode());
        assertEquals(DiaryErrorCode.DIARY_ALREADY_EXISTS.defaultMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("일기 상세 조회 성공")
    void getDiary_success() {
        // Given
        when(diaryRepository.findById(1L)).thenReturn(Optional.of(diary1));

        // When
        DiaryResponse response = diaryService.getDiary(userA, 1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(diary1.getId(), response.id());
    }

    @Test
    @DisplayName("일기 조회 실패 - 일기가 존재하지 않음")
    void getDiary_failure_notFound() {
        // Given
        when(diaryRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        DiaryException e = assertThrows(DiaryException.class, () -> {
            diaryService.getDiary(userA, 1L);
        });

        // Then
        assertEquals(DiaryErrorCode.DIARY_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("일기 조회 실패 - 다른 사람의 일기에 접근하는 경우")
    void getDiary_failure_forbiddenUser() {
        // Given
        when(diaryRepository.findById(3L)).thenReturn(Optional.of(otherDiary));

        // When & Then
        DiaryException e = assertThrows(DiaryException.class, () -> {
            diaryService.getDiary(userA, 3L);
        });

        assertEquals(DiaryErrorCode.DIARY_ACCESS_DENIED, e.getErrorCode());
    }

    @Test
    @DisplayName("일기 내용 수정 성공")
    void updateContent_success() {
        // Given
        when(diaryRepository.findById(1L)).thenReturn(Optional.of(diary1));
        when(diaryRepository.save(diary1)).thenReturn(diary1);

        // When
        DiaryResponse response = diaryService.updateContent(userA, 1L, contentUpdateRequest);

        // Then
        assertNotNull(response);
        assertEquals(contentUpdateRequest.content(), response.content());
    }

    @Test
    @DisplayName("일기 수정 실패 - 일기가 존재하지 않음")
    void updateContent_failure_notFound() {
        // Given
        when(diaryRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        DiaryException e = assertThrows(DiaryException.class, () -> {
            diaryService.updateContent(userA, 1L, contentUpdateRequest);
        });

        // then
        assertEquals(DiaryErrorCode.DIARY_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("일기 수정 실패 - 다른 사람의 일기에 접근하는 경우")
    void updateContent_failure_forbiddenUser() {
        // Given
        when(diaryRepository.findById(3L)).thenReturn(Optional.of(otherDiary));

        // When & Then
        DiaryException e = assertThrows(DiaryException.class, () -> {
            diaryService.updateContent(userA, 3L, contentUpdateRequest);
        });

        assertEquals(DiaryErrorCode.DIARY_ACCESS_DENIED, e.getErrorCode());
    }

    @Test
    @DisplayName("월별 일기 목록 조회 성공")
    void getMonthlyDiaryList_success() {
        // Given
        YearMonth yearMonth = YearMonth.of(2024, 9);
        List<Diary> diaryList = Arrays.asList(
                diary1, diary2
        );

        when(diaryRepository.findByMonthAndYear(1L, yearMonth.getMonthValue(), yearMonth.getYear()))
                .thenReturn(diaryList);

        // When
        List<DiaryResponse> response = diaryService.getMonthlyDiaryList(userA, yearMonth);

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(diaryRequest.content(), response.get(0).content());
        assertEquals(diaryRequest_tomorrow.content(), response.get(1).content());
    }
}
