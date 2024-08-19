package com.muud.playlist.repository;

import com.muud.emotion.domain.Emotion;
import com.muud.playlist.domain.PlayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayListRepository extends JpaRepository<PlayList, Long> {

    @Query("SELECT p from PlayList p where p.emotion = :emotion order by rand()")
    Page<PlayList> findByEmotion(Emotion emotion, Pageable pageable);

    Optional<PlayList> findByVideoId(String videoId);

    @Modifying
    @Query("DELETE FROM PlayList p WHERE p.videoId IN :videoIds")
    void deleteByVideoIds(@Param("videoIds") List<String> videoIds);

    @Query("SELECT p.videoId FROM PlayList p")
    List<String> findAllVideoIds();
}
