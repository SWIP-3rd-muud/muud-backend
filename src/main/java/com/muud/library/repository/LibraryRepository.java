package com.muud.library.repository;

import com.muud.library.domain.entity.Library;
import com.muud.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    Page<Library> findByOwner(User user, Pageable pageable);
}
