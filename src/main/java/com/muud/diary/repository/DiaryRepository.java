package com.muud.diary.repository;

import com.muud.diary.domain.Diary;
import com.muud.emotion.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE MONTH(d.createdDate) = :month AND YEAR(d.createdDate) = :year")
    List<Diary> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT d FROM Diary d WHERE d.emotion = :emotion")
    List<Diary> findByEmotion(@Param("emotion") Emotion emotion);
}