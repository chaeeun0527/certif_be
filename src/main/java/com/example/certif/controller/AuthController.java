package com.example.certif.controller;

import com.example.certif.dto.LoginRequest;
import com.example.certif.dto.SignupRequest;
import com.example.certif.entity.User;
import com.example.certif.service.AuthService;
import com.example.certif.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest request) {
        User user = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "");
        authService.logout(pureToken);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "");
        String newToken = authService.refreshToken(pureToken);
        return ResponseEntity.ok(newToken);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = authService.checkEmail(email);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = authService.checkNickname(nickname);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestParam String email) {
        authService.deleteAccount(email);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    // ===== 비밀번호 재설정 2개 엔드포인트 =====

    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("email is required");
        }
        authService.sendPasswordResetToken(email);
        return ResponseEntity.ok("Password reset token issued (check server logs).");
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        if (token == null || token.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("token and newPassword are required");
        }
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password changed.");
    }
}
