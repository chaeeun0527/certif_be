package com.example.certif.service;

import com.example.certif.entity.User;
import com.example.certif.repository.CommentRepository;
import com.example.certif.repository.PostRepository;
import com.example.certif.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 프로필 이미지 저장 기본 폴더 (서버 배포 환경에서도 사용 가능)
    private final Path baseFolder = Paths.get(System.getProperty("user.home"), "app-data", "profile-images");

    public User getMyInfo(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    // 비밀번호 재설정 요청
    public void sendPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // UUID 기반 토큰 생성
        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(1)); // 1시간 유효
        userRepository.save(user);

        // 이메일 발송 (여기서는 콘솔 출력)
        System.out.println("=== 비밀번호 재설정 이메일 ===");
        System.out.println("수신자: " + email);
        System.out.println("토큰: " + token);
        System.out.println("재설정 링크: http://localhost:8080/reset-password?token=" + token);
        System.out.println("============================");
    }

     // 비밀번호 재설정
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰입니다."));

        if (user.getPasswordResetExpiry() == null || user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("토큰이 만료되었습니다.");
        }

        // BCrypt로 비밀번호 암호화 후 저장
        user.setPassword(passwordEncoder.encode(newPassword));

        // 토큰 초기화
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);

        userRepository.save(user);
    }


    public List<String> getMyComments(String email) {
        return commentRepository.findByUserEmail(email).stream()
                .map(comment -> comment.getContent()) // 또는 dto로 변환
                .toList();
    }

    public void updateComment(Long id, String content) {
        var comment = commentRepository.findById(id).orElseThrow();
        comment.setContent(content);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<String> getMyPosts(String email) {
        return postRepository.findByUserEmail(email).stream()
                .map(post -> post.getTitle()) // 또는 dto로 변환
                .toList();
    }

    public void updatePost(Long id, String content) {
        var post = postRepository.findById(id).orElseThrow();
        post.setContent(content);
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    // 프로필 이미지 업로드
    public void uploadProfileImage(String email, MultipartFile image) {

        // 폴더가 없으면 생성
        if (!Files.exists(baseFolder)) {
            try {
                Files.createDirectories(baseFolder);
            } catch (IOException e) {
                throw new RuntimeException("폴더 생성 실패", e);
            }
        }

        // 저장할 파일 이름: 이메일_현재시간.확장자
        String filename = email + "_" + System.currentTimeMillis() + getExtension(image.getOriginalFilename());
        Path filePath = baseFolder.resolve(filename);

        // 파일 저장
        try {
            image.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // DB 업데이트
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setProfileImageUrl(filePath.toString());
        userRepository.save(user);
    }

    // 기존 이미지 제거 후 변경
    public void updateProfileImage(String email, MultipartFile image) {
        User user = userRepository.findByEmail(email).orElseThrow();

        deleteExistingFile(user.getProfileImageUrl());

        // 새 이미지 업로드
        uploadProfileImage(email, image);
    }

    public void deleteProfileImage(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        deleteExistingFile(user.getProfileImageUrl());
        user.setProfileImageUrl(null);
        userRepository.save(user);
    }

    // 파일 삭제 유틸
    private void deleteExistingFile(String pathStr) {
        if (pathStr == null) return;
        Path path = Paths.get(pathStr);
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패", e);
        }
    }

    // 파일 확장자 추출
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }

}
