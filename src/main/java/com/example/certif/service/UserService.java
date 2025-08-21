package com.example.certif.service;

import com.example.certif.dto.MyCommentDto;
import com.example.certif.dto.MyPostDto;
import com.example.certif.entity.User;
import com.example.certif.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudyPostRepository studyPostRepository;
    private final SharePostRepository sharePostRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final ShareCommentRepository shareCommentRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 프로필 이미지 저장 기본 폴더
    private final Path baseFolder = Paths.get(System.getProperty("user.home"), "app-data", "profile-images");

    // 사용자 정보
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

    // 내가 쓴 게시글 목록 조회
    public List<MyPostDto> getMyPosts(String email) {
        List<MyPostDto> result = new ArrayList<>();
        studyPostRepository.findByUser_Email(email).forEach(p -> result.add(MyPostDto.fromEntity(p, "study")));
        sharePostRepository.findByUser_Email(email).forEach(p -> result.add(MyPostDto.fromEntity(p, "share")));
        return result;
    }

    // 내가 쓴 특정 게시글 조회
    public MyPostDto getMyPost(String email, Long postId, String type) throws AccessDeniedException {
        if ("study".equals(type)) { // 스터디 모집 글
            var post = studyPostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
            if (!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            return MyPostDto.fromEntity(post, "study");
        }
        else if ("share".equals(type)) { // 공유마당 글
            var post = sharePostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
            if (!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            return MyPostDto.fromEntity(post, "share");
        } else throw new IllegalArgumentException("올바른 게시판 타입이 아닙니다.");
    }

    // 내가 쓴 게시글 수정
    @Transactional
    public void updatePost(Long postId, String title, String content, String type, String email) throws AccessDeniedException {
        if ("study".equals(type)) {
            var post = studyPostRepository.findById(postId).orElseThrow();
            if (!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            post.setTitle(title);
            post.setContent(content);
            studyPostRepository.save(post);
        }
        else if ("share".equals(type)) {
            var post = sharePostRepository.findById(postId).orElseThrow();
            if (!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            post.setTitle(title);
            post.setContent(content);
            sharePostRepository.save(post);
        } else throw new IllegalArgumentException("올바른 게시판 타입이 아닙니다.");
    }

    // 내가 쓴 게시글 삭제
    @Transactional
    public void deletePost(Long postId, String type, String email) throws AccessDeniedException {
        if ("study".equals(type)) {
            var post = studyPostRepository.findById(postId).orElseThrow();
            if (!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            studyPostRepository.delete(post);
        }
        else if ("share".equals(type)) {
            var post = sharePostRepository.findById(postId).orElseThrow();
            if (!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            sharePostRepository.delete(post);
        } else throw new IllegalArgumentException("올바른 게시판 타입이 아닙니다.");
    }

    // 내가 쓴 댓글 목록 조회
    public List<MyCommentDto> getMyComments(String email) {
        List<MyCommentDto> result = new ArrayList<>();
        studyCommentRepository.findByUser_Email(email).forEach(c -> result.add(MyCommentDto.fromEntity(c, "study")));
        shareCommentRepository.findByUser_Email(email).forEach(c -> result.add(MyCommentDto.fromEntity(c, "share")));
        return result;
    }

    // 내가 쓴 특정 댓글 조회
    public MyCommentDto getMyComment(String email, Long commentId, String type) throws AccessDeniedException {
        if ("study".equals(type)) {
            var comment = studyCommentRepository.findById(commentId).orElseThrow();
            if (!comment.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            return MyCommentDto.fromEntity(comment, "study");
        }
        else if ("share".equals(type)) {
            var comment = shareCommentRepository.findById(commentId).orElseThrow();
            if (!comment.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            return MyCommentDto.fromEntity(comment, "share");
        } else throw new IllegalArgumentException("올바른 댓글 타입이 아닙니다.");
    }

    // 내가 쓴 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String content, String type, String email) throws AccessDeniedException {
        if ("study".equals(type)) {
            var comment = studyCommentRepository.findById(commentId).orElseThrow();
            if (!comment.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            comment.setContent(content);
            studyCommentRepository.save(comment);
        }
        else if ("share".equals(type)) {
            var comment = shareCommentRepository.findById(commentId).orElseThrow();
            if (!comment.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            comment.setContent(content);
            shareCommentRepository.save(comment);
        } else throw new IllegalArgumentException("올바른 댓글 타입이 아닙니다.");
    }

    // 내가 쓴 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String type, String email) throws AccessDeniedException {
        if ("study".equals(type)) {
            var comment = studyCommentRepository.findById(commentId).orElseThrow();
            if (!comment.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            studyCommentRepository.delete(comment);
        }
        else if ("share".equals(type)) {
            var comment = shareCommentRepository.findById(commentId).orElseThrow();
            if (!comment.getUser().getEmail().equals(email)) throw new AccessDeniedException("권한 없음");
            shareCommentRepository.delete(comment);
        } else throw new IllegalArgumentException("올바른 댓글 타입이 아닙니다.");
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
