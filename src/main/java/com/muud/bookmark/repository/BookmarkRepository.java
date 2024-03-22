package com.muud.bookmark.repository;

import com.muud.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(Long userId);

    Optional<Bookmark> findByUserIdAndDiaryId(Long userId, Long diaryId);
}