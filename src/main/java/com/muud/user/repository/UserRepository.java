package com.muud.user.repository;

import com.muud.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndSocialId(String email, String socialId);
    Optional<User> findByIdAndEmail(Long id, String email);
}
