package com.example.certif.service;

import com.example.certif.dto.LoginRequest;
import com.example.certif.dto.SignupRequest;
import com.example.certif.entity.User;
import com.example.certif.repository.UserRepository;
import com.example.certif.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Set<String> blacklist = new HashSet<>();

    // ✅ 공통 정규화 유틸
    private String normalizeEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase();
    }

    public User signup(SignupRequest request) {
        final String email = normalizeEmail(request.getEmail());
        final String nickname = request.getNickname() == null ? null : request.getNickname().trim();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        final String email = normalizeEmail(request.getEmail());
        return userRepository.findByEmailIgnoreCase(email)
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다."));
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(normalizeEmail(email)); // ✅ 변경
    }

    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname == null ? null : nickname.trim());
    }

    public void deleteAccount(String email) {
        User user = userRepository.findByEmailIgnoreCase(normalizeEmail(email)) // ✅ 변경
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    public void logout(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public String refreshToken(String oldToken) {
        if (!jwtUtil.validateToken(oldToken) || isTokenBlacklisted(oldToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
        String email = normalizeEmail(jwtUtil.getEmailFromToken(oldToken));
        User user = userRepository.findByEmailIgnoreCase(email) // ✅ 변경
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return jwtUtil.generateAccessToken(user);
    }

    public String generateAccessToken(User user) {
        return jwtUtil.generateAccessToken(user);
    }
}
