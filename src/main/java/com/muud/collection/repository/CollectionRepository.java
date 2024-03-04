package com.muud.collection.repository;

import com.muud.collection.entity.Collection;
import com.muud.playlist.entity.PlayList;
import com.muud.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Page<Collection> findByUser(User user, Pageable pageable);
    Optional<Collection> findByUserAndPlayList(User user, PlayList playList);
    Page<Collection> findByUserAndLiked(User user, Boolean liked, Pageable pageable);
}
