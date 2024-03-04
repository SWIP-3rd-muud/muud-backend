package com.muud.collection.repository;

import com.muud.collection.entity.Collection;
import com.muud.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Page<Collection> findByUser(User user, Pageable pageable);
    @Query("select c from Collection c where c.videoId = :videoId")
    Optional<Collection> findByVideoId(String videoId);

    Optional<Collection> findByUserAndVideoId(User user, String videoId);
}
