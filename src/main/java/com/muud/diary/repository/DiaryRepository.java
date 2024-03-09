package com.muud.diary.repository;

import com.muud.diary.domain.Diary;
import com.muud.emotion.domain.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d " +
            "FROM Diary d " +
            "WHERE d.user.id = :userId " +
            "AND MONTH(d.referenceDate) = :month " +
            "AND YEAR(d.referenceDate) = :year")
    List<Diary> findByMonthAndYear(@Param("userId") final Long userId,
                                   @Param("month") final int month,
                                   @Param("year") final int year);

    @Query("SELECT d " +
            "FROM Diary d " +
            "WHERE d.user.id = :userId " +
            "AND d.emotion = :emotion")
    List<Diary> findByEmotion(@Param("userId") final Long userId,
                              @Param("emotion") final Emotion emotion);

    @Query("SELECT d FROM Diary d WHERE d.user.id = :userId")
    List<Diary> findByUserId(@Param("userId") final Long userId);

    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user.id = :userId AND d.referenceDate = :referenceDate")
    int countDiariesByUserIdAndReferenceDate(@Param("userId") final Long userId,
                                             @Param("referenceDate") final LocalDate referenceDate);
}