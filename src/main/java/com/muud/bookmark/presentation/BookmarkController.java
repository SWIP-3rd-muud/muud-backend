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

    @GetMapping("/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> getBookmarkResponseList(@RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.ok(bookmarkService.getBookmarkResponseListByUser(bookmarkRequest.userId()));
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<Object> addBookmark(@RequestBody BookmarkRequest bookmarkRequest) {
        Bookmark bookmark = bookmarkService.addBookmark(bookmarkRequest.userId(), bookmarkRequest.diaryId());
        return ResponseEntity.created(URI.create("/bookmarks/"+bookmark.getId())).build();
    }

    @GetMapping("/bookmarks/{bookmarkId}")
    public ResponseEntity<Boolean> checkBookmark(@RequestBody BookmarkRequest bookmarkRequest) {
        boolean isBookmarked = bookmarkService.isBookmarkedByUser(bookmarkRequest.userId(), bookmarkRequest.diaryId());
        return ResponseEntity.ok(isBookmarked);
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public ResponseEntity<Object> removeBookmark(@PathVariable("bookmarkId") Long bookmarkId) {
        bookmarkService.removeBookmark(bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
