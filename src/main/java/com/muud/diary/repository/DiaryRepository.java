package com.muud.diary.repository;

import com.muud.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE MONTH(d.createdDate) = :month")
    List<Diary> findByMonth(@Param("month") int month);
}
