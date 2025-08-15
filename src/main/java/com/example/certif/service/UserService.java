package com.example.certif.service;

import com.example.certif.entity.User;
import com.example.certif.repository.CommentRepository;
import com.example.certif.repository.PostRepository;
import com.example.certif.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Value("${user.profile-image.dir:uploads/profile-images}")
    private String profileImageDir;

    public User getMyInfo(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void sendPasswordResetToken(String email) {
        // 이메일로 토큰 발송 로직 (생략)
    }

    public void resetPassword(String token, String newPassword) {
        // 토큰 검증 및 비밀번호 변경 로직 (생략)
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

    // 프로필 이미지
    public void uploadProfileImage(String email, MultipartFile image) {

        // 저장할 폴더 경로
        String folder = System.getProperty("java.io.tmpdir") + "/uploads/profile-images";
        Path folderPath = Paths.get(folder);

        // 폴더가 없으면 생성
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                throw new RuntimeException("폴더 생성 실패", e);
            }
        }

        // 저장할 파일 이름: 이메일_현재시간.확장자
        String filename = email + "_" + System.currentTimeMillis() + ".png"; // 예시는 png
        Path filePath = folderPath.resolve(filename);

        try {
            image.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setProfileImageUrl(filePath.toString());
        userRepository.save(user);
    }

    public void updateProfileImage(String email, MultipartFile image) {
        User user = userRepository.findByEmail(email).orElseThrow();
        deleteExistingFile(user.getProfileImageUrl());
        String filename = saveFile(image, email);
        user.setProfileImageUrl("/profile-images/" + filename);
        userRepository.save(user);
    }

    public void deleteProfileImage(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        deleteExistingFile(user.getProfileImageUrl());
        user.setProfileImageUrl(null);
        userRepository.save(user);
    }

    // 파일 처리
    private String saveFile(MultipartFile file, String email) {
        try {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";

            String filename = email + "_" + System.currentTimeMillis() + ext;
            Path uploadPath = Paths.get(profileImageDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
    }

    private void deleteExistingFile(String url) {
        if (url == null || url.isBlank()) return;

        String filename = url.substring(url.lastIndexOf("/") + 1);
        Path filePath = Paths.get(profileImageDir).resolve(filename);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // 로그 남기고 무시 가능
            e.printStackTrace();
        }
    }
}
