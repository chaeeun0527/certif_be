package com.example.certif.service;

import com.example.certif.dto.MyCommentDto;
import com.example.certif.dto.MyPostDto;
import com.example.certif.entity.*;
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
    public User getMyInfo(Long userId) {
        return userRepository.findById(userId).orElseThrow();
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
    public List<MyPostDto> getMyPosts(Long userId) {
        // 스터디 글
        List<MyPostDto> studyDtos = studyPostRepository.findByUserId(userId).stream()
                .map(post -> MyPostDto.fromEntity(post, "study"))
                .toList();

        // 공유 글
        List<MyPostDto> shareDtos = sharePostRepository.findByUserId(userId).stream()
                .map(post -> MyPostDto.fromEntity(post, "share"))
                .toList();

        // 합치기
        List<MyPostDto> result = new ArrayList<>();
        result.addAll(studyDtos);
        result.addAll(shareDtos);

        return result;
    }

    // 공통 소유자 검증 메서드
    private void validateOwnership(Long ownerId, Long userId) throws AccessDeniedException {
        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("권한 없음");
        }
    }

    // 내가 쓴 특정 게시글 조회
    public MyPostDto getMyPost(Long userId, Long postId, String type) throws AccessDeniedException {
        if ("study".equals(type)) {
            StudyPost post = studyPostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
            validateOwnership(post.getUser().getId(), userId);
            return MyPostDto.fromEntity(post, "study");
        } else if ("share".equals(type)) {
            SharePost post = sharePostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
            validateOwnership(post.getUser().getId(), userId);
            return MyPostDto.fromEntity(post, "share");
        }
        throw new IllegalArgumentException("올바른 게시판 타입이 아닙니다.");
    }

    // 내가 쓴 게시글 수정
    @Transactional
    public MyPostDto updatePost(Long postId, String title, String content, String type, Long userId) throws AccessDeniedException {
        if ("study".equals(type)) {
            StudyPost target = studyPostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

            validateOwnership(target.getUser().getId(), userId);

            target.setTitle(title);
            target.setContent(content);
            StudyPost updatedPost = studyPostRepository.save(target);
            return MyPostDto.fromEntity(updatedPost, "study");

        } else if ("share".equals(type)) {
            SharePost target = sharePostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

            validateOwnership(target.getUser().getId(), userId);

            target.setTitle(title);
            target.setContent(content);
            SharePost updatedPost = sharePostRepository.save(target);
            return MyPostDto.fromEntity(updatedPost, "share");

        } else {
            throw new IllegalArgumentException("올바른 게시판 타입이 아닙니다.");
        }
    }


    // 내가 쓴 게시글 삭제
    @Transactional
    public void deletePost(Long postId, String type, Long userId) throws AccessDeniedException {
        if ("study".equals(type)) {
            StudyPost target = studyPostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

            validateOwnership(target.getUser().getId(), userId);
            studyPostRepository.delete(target);

        } else if ("share".equals(type)) {
            SharePost target = sharePostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

            validateOwnership(target.getUser().getId(), userId);
            sharePostRepository.delete(target);

        } else {
            throw new IllegalArgumentException("올바른 게시판 타입이 아닙니다.");
        }
    }

    // 내가 쓴 댓글 목록 조회
    public List<MyCommentDto> getMyComments(Long userId) {
        List<MyCommentDto> studyDtos = studyCommentRepository.findByUserId(userId).stream()
                .map(comment -> MyCommentDto.fromEntity(comment, "study"))
                .toList();

        List<MyCommentDto> shareDtos = shareCommentRepository.findByUserId(userId).stream()
                .map(comment -> MyCommentDto.fromEntity(comment, "share"))
                .toList();

        List<MyCommentDto> result = new ArrayList<>();
        result.addAll(studyDtos);
        result.addAll(shareDtos);

        return result;
    }


    // 내가 쓴 특정 댓글 조회
    public MyCommentDto getMyComment(Long userId, Long commentId, String type) throws AccessDeniedException {
        if ("study".equals(type)) {
            StudyComment comment = studyCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
            validateOwnership(comment.getUser().getId(), userId);
            return MyCommentDto.fromEntity(comment, "study");
        } else if ("share".equals(type)) {
            ShareComment comment = shareCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
            validateOwnership(comment.getUser().getId(), userId);
            return MyCommentDto.fromEntity(comment, "share");
        }
        throw new IllegalArgumentException("올바른 댓글 타입이 아닙니다.");
    }

    // 내가 쓴 댓글 수정
    @Transactional
    public MyCommentDto updateComment(Long commentId, String content, String type, Long userId) throws AccessDeniedException {
        if ("study".equals(type)) {
            StudyComment target = studyCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

            validateOwnership(target.getUser().getId(), userId);
            target.setContent(content);
            StudyComment updatedComment = studyCommentRepository.save(target);
            return MyCommentDto.fromEntity(updatedComment, "study");

        } else if ("share".equals(type)) {
            ShareComment target = shareCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

            validateOwnership(target.getUser().getId(), userId);
            target.setContent(content);
            ShareComment updatedComment = shareCommentRepository.save(target);
            return MyCommentDto.fromEntity(updatedComment, "share");

        } else {
            throw new IllegalArgumentException("올바른 댓글 타입이 아닙니다.");
        }
    }

    // 내가 쓴 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String type, Long userId) throws AccessDeniedException {
        if ("study".equals(type)) {
            StudyComment target = studyCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

            validateOwnership(target.getUser().getId(), userId);
            studyCommentRepository.delete(target);

        } else if ("share".equals(type)) {
            ShareComment target = shareCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

            validateOwnership(target.getUser().getId(), userId);
            shareCommentRepository.delete(target);

        } else {
            throw new IllegalArgumentException("올바른 댓글 타입이 아닙니다.");
        }
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
