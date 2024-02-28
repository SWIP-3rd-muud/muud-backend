package com.muud.collection.repositoty;

import com.muud.collection.entity.Collection;
import com.muud.playlist.entity.PlayList;
import com.muud.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Optional<Collection> findById(Long id);

    Page<Collection> findByUser(User user, Pageable pageable);
    @Query("select c from Collection c where c.playList.videoId = :videoId")
    Optional<Collection> findByPlayList(String videoId);

    Optional<Collection> findByUserAndPlayList(User user, PlayList playList);
}
