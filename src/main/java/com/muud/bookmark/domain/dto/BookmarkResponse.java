package com.muud.bookmark.domain.dto;

import com.muud.bookmark.domain.Bookmark;

public record BookmarkResponse(Long bookmarkId,
                               Long diaryId) {
    public static BookmarkResponse from(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getDiary().getId());
    }
}
