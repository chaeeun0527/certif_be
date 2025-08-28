package com.example.certif.repository;

import com.example.certif.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);   // ✅ 변경
    boolean existsByNickname(String nickname);
    Optional<User> findByEmailIgnoreCase(String email); // ✅ 변경
}
