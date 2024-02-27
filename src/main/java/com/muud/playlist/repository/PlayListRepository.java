package com.muud.playlist.repository;

import com.muud.emotion.entity.Emotion;
import com.muud.playlist.entity.PlayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    @Query("SELECT p from PlayList p where p.emotion = :emotion order by rand()")
    Page<PlayList> findByEmotion(Emotion emotion, Pageable pageable);
}
