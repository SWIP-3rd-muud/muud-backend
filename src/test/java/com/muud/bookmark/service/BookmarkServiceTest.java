package com.muud.bookmark.service;

import com.muud.bookmark.domain.Bookmark;
import com.muud.bookmark.repository.BookmarkRepository;
import com.muud.diary.domain.Diary;
import com.muud.diary.domain.dto.DiaryRequest;
import com.muud.diary.repository.DiaryRepository;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static com.muud.bookmark.exception.BookmarkErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DiaryRepository diaryRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private User user;
    private Diary diary;
    private DiaryRequest diaryRequest;
    private PlayList playList;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@email.com")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        diaryRequest = new DiaryRequest("diary1`", "JOY",
                LocalDate.of(2024, 9, 20), 1L);

        playList = PlayList.builder()
                .videoId("video123")
                .build();
        ReflectionTestUtils.setField(playList, "id", 1L);

        diary = Diary.of(user, diaryRequest, null, playList);
        ReflectionTestUtils.setField(diary, "id", 1L);
    }

    @Test
    @DisplayName("북마크 등록 성공")
    void addBookmark_success() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(diaryRepository.findById(diary.getId())).thenReturn(Optional.of(diary));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(new Bookmark(user, diary));

        // when
        Bookmark response = bookmarkService.addBookmark(user.getId(), diary.getId());

        // then
        assertNotNull(response);
        assertEquals(user.getId(), response.getUser().getId());
        assertEquals(diary.getId(), response.getDiary().getId());
    }

    @Test
    @DisplayName("북마크 등록 실패 - 유저가 존재하지 않음")
    void addBookmark_fail_userNotFound() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(USER_NOT_FOUND.defaultException().getClass(),
                () -> bookmarkService.addBookmark(user.getId(), diary.getId()));
    }

    @Test
    @DisplayName("북마크 등록 실패 - 일기가 존재하지 않음")
    void addBookmark_fail_diaryNotFound() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(diaryRepository.findById(diary.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(DIARY_NOT_FOUND.defaultException().getClass(),
                () -> bookmarkService.addBookmark(user.getId(), diary.getId()));
    }

    @Test
    @DisplayName("북마크 등록 실패 - 이미 북마크가 존재함")
    void addBookmark_fail_whenBookmarkAlreadyExist() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(diaryRepository.findById(diary.getId())).thenReturn(Optional.of(diary));
        when(bookmarkRepository.findByUserIdAndDiaryId(user.getId(), diary.getId()))
                .thenReturn(Optional.of(new Bookmark(user, diary)));

        // when & then
        assertThrows(BOOKMARK_ALREADY_EXISTS.defaultException().getClass(),
                () -> bookmarkService.addBookmark(user.getId(), diary.getId()));
    }
}