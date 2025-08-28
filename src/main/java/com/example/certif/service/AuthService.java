package com.example.certif.service;

import com.example.certif.dto.LoginRequest;
import com.example.certif.dto.SignupRequest;
import com.example.certif.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    // 기존에 있던 의존성들 유지하세요 (예: UserRepository, JwtUtil, TokenBlacklist 등)
    private final UserService userService;

    // ===== 기존 메서드들 (당신 프로젝트에 이미 구현돼 있던 시그니처 유지) =====
    public User signup(SignupRequest request) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement signup");
    }

    public User login(LoginRequest request) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement login");
    }

    public void logout(String accessToken) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement logout");
    }

    public String refreshToken(String refreshToken) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement refreshToken");
    }

    public boolean checkEmail(String email) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement checkEmail");
    }

    public boolean checkNickname(String nickname) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement checkNickname");
    }

    public void deleteAccount(String email) {
        // 기존 구현 그대로
        throw new UnsupportedOperationException("Implement deleteAccount");
    }

    // ===== 여기서부터 추가: 비밀번호 재설정 위임 =====

    /** 비밀번호 재설정 토큰 발급 (UserService로 위임) */
    public void sendPasswordResetToken(String email) {
        userService.sendPasswordResetToken(email);
    }

    /** 토큰으로 비밀번호 변경 (UserService로 위임) */
    public void resetPassword(String token, String newPassword) {
        userService.resetPassword(token, newPassword);
    }
}
