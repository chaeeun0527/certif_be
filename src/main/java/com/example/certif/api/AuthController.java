package com.example.certif.api;

import com.example.certif.dto.LoginRequest;
import com.example.certif.dto.SignupRequest;
import com.example.certif.entity.User;
import com.example.certif.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest request) {
        User user = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        return ResponseEntity.ok(user);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 실제 서비스에서는 JWT 토큰을 블랙리스트 처리하는 로직이 필요
        return ResponseEntity.ok("로그아웃 성공");
    }

    // 토큰 갱신
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() {
        // JWT 토큰 갱신 로직 필요
        return ResponseEntity.ok("토큰 갱신 성공");
    }

    // 회원 탈퇴
    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@RequestParam String email) {
        authService.deleteAccount(email);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = authService.checkEmail(email);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    // 닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isDuplicate = authService.checkNickname(nickname);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }
}
