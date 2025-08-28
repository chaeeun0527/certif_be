package com.example.certif.repository;

import com.example.certif.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);

    // 비밀번호 재설정 토큰으로 사용자 조회
    Optional<User> findByPasswordResetToken(String token);
}
