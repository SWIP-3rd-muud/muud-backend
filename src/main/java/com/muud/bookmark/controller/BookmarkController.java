package com.muud.bookmark.controller;

import com.muud.bookmark.service.BookmarkService;
import com.muud.bookmark.domain.Bookmark;
import com.muud.bookmark.domain.dto.BookmarkResponse;
import com.muud.global.util.SecurityUtils;
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

    @GetMapping("/diaries/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> getBookmarkList() {
        return ResponseEntity.ok(bookmarkService.getBookmarkList(getCurrentUser().getId()));
    }

    @GetMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Boolean> checkBookmark(@PathVariable("diaryId") final Long diaryId) {
        boolean isBookmarked = bookmarkService.isBookmarked(getCurrentUser().getId(), diaryId);
        return ResponseEntity.ok(isBookmarked);
    }

    @PostMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Object> addBookmark(@PathVariable("diaryId") final Long diaryId) {
        Bookmark bookmark = bookmarkService.addBookmark(getCurrentUser().getId(), diaryId);
        return ResponseEntity.created(URI.create("/bookmarks/"+bookmark.getId())).build();
    }

    @DeleteMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Object> removeBookmark(@PathVariable("diaryId") final Long diaryId) {
        bookmarkService.removeBookmark(getCurrentUser().getId(), diaryId);
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }
}