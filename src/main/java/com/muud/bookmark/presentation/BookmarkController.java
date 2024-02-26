package com.muud.bookmark.presentation;

import com.muud.bookmark.application.BookmarkService;
import com.muud.bookmark.domain.Bookmark;
import com.muud.bookmark.domain.dto.BookmarkRequest;
import com.muud.bookmark.domain.dto.BookmarkResponse;
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
    public ResponseEntity<List<BookmarkResponse>> getBookmarkResponseList(@RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.getBookmarkResponseListByUser(bookmarkRequest.userId()));
    }

    @GetMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Boolean> checkBookmark(@PathVariable("diaryId") Long diaryId,
                                                 @RequestBody BookmarkRequest bookmarkRequest) {
        boolean isBookmarked = bookmarkService.isBookmarkedByUser(bookmarkRequest.userId(), diaryId);
        return ResponseEntity.ok(isBookmarked);
    }

    @PostMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Object> addBookmark(@PathVariable("diaryId") Long diaryId,
                                              @RequestBody BookmarkRequest bookmarkRequest) {
        Bookmark bookmark = bookmarkService.addBookmark(bookmarkRequest.userId(), diaryId);
        return ResponseEntity.created(URI.create("/bookmarks/"+bookmark.getId())).build();
    }

    @DeleteMapping("/diaries/{diaryId}/bookmarks")
    public ResponseEntity<Object> removeBookmark(@PathVariable("diaryId") Long diaryId,
                                                 @RequestBody BookmarkRequest bookmarkRequest) {
        bookmarkService.removeBookmark(bookmarkRequest.userId(), diaryId);
        return ResponseEntity.noContent().build();
    }
}
