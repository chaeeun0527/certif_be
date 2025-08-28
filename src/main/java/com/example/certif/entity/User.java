package com.example.certif.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profileImage;

    private String passwordResetToken;
    private LocalDateTime passwordResetExpiry;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성 시 자동 시간 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 수정 시 자동 시간 갱신
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // jwtUtil 등에서 사용되는 명시적 getter (가독성 + 안정성 ↑)
    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Long getId() {
        return this.id;
    }

    public void setProfileImageUrl(String url) {
        this.profileImage = url;
    }

    public String getProfileImageUrl() {
        return this.profileImage;
    }

    // 비밀번호 재설정 토큰 관련
    public void setPasswordResetToken(String token) {
        this.passwordResetToken = token;
    }

    public String getPasswordResetToken() {
        return this.passwordResetToken;
    }

    public void setPasswordResetExpiry(LocalDateTime expiry) {
        this.passwordResetExpiry = expiry;
    }

    public LocalDateTime getPasswordResetExpiry() {
        return this.passwordResetExpiry;
    }
}
