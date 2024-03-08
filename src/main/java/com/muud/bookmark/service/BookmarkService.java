package com.muud.bookmark.service;

import com.muud.bookmark.domain.Bookmark;
import com.muud.bookmark.domain.dto.BookmarkResponse;
import com.muud.bookmark.repository.BookmarkRepository;
import com.muud.diary.domain.Diary;
import com.muud.diary.repository.DiaryRepository;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.user.entity.User;
import com.muud.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public Bookmark addBookmark(Long userId, Long diaryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ApiException(ExceptionType.NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(()->new ApiException(ExceptionType.NOT_FOUND));

        if (isBookmarkedByUser(userId, diaryId)) {
            throw new ApiException(ExceptionType.BAD_REQUEST);
        }
        return bookmarkRepository.save(new Bookmark(user, diary));
    }

    @Transactional
    public void removeBookmark(Long userId, Long diaryId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndDiaryId(userId, diaryId)
                .orElseThrow(() -> new ApiException(ExceptionType.NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

    public List<BookmarkResponse> getBookmarkResponseListByUser(Long userId) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByUserId(userId);
        return bookmarkList.stream()
                .map(BookmarkResponse::from)
                .collect(Collectors.toList());
    }

    public boolean isBookmarkedByUser(Long userId, Long postId) {
        return bookmarkRepository.findByUserIdAndDiaryId(userId, postId).isPresent();
    }
}


