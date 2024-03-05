package com.muud.bookmark.presentation;

import com.muud.auth.jwt.Auth;
import com.muud.bookmark.application.BookmarkService;
import com.muud.bookmark.domain.Bookmark;
import com.muud.bookmark.domain.dto.BookmarkResponse;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Auth
    @GetMapping("/diaries/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> getBookmarkResponseList(@RequestAttribute("user") User user) {
        return ResponseEntity.ok(bookmarkService.getBookmarkResponseListByUser(user.getId()));
    }

    @Auth
    @GetMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Boolean> checkBookmark(@RequestAttribute("user") User user,
                                                 @PathVariable("diaryId") Long diaryId) {
        boolean isBookmarked = bookmarkService.isBookmarkedByUser(user.getId(), diaryId);
        return ResponseEntity.ok(isBookmarked);
    }

    @Auth
    @PostMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Object> addBookmark(@RequestAttribute("user") User user,
                                              @PathVariable("diaryId") Long diaryId) {
        Bookmark bookmark = bookmarkService.addBookmark(user.getId(), diaryId);
        return ResponseEntity.created(URI.create("/bookmarks/"+bookmark.getId())).build();
    }

    @Auth
    @DeleteMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Object> removeBookmark(@RequestAttribute("user") User user,
                                                 @PathVariable("diaryId") Long diaryId) {
        bookmarkService.removeBookmark(user.getId(), diaryId);
        return ResponseEntity.noContent().build();
    }
}
