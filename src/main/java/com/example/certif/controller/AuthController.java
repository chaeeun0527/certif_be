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

    // ✅ 이름 명시 + 서비스에서 정규화 처리
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean isDuplicate = authService.checkEmail(email);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = authService.checkNickname(nickname);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestParam("email") String email) {
        authService.deleteAccount(email);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}
