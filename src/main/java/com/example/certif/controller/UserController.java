package com.example.certif.controller;

import com.example.certif.dto.*;
import com.example.certif.entity.User;
import com.example.certif.security.UserPrincipal;
import com.example.certif.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 마이페이지
    @GetMapping("/my-page")
    public ResponseEntity<User> getMyPage(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getMyInfo(principal.getUserId()));
    }

    // 비밀번호 재설정 요청 메일 보내기
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        String email = principal.getUsername();
        userService.sendPasswordResetToken(email);
        return ResponseEntity.ok("비밀번호 재설정 이메일 전송 완료");
    }

    // 비밀번호 재설정
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다");
    }

    // 내가 쓴 게시글 목록 조회
    @GetMapping("/my-posts")
    public ResponseEntity<List<MyPostDto>> getMyPosts(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getMyPosts(principal.getUserId()));
    }

    // 내가 쓴 특정 게시글 조회
    @GetMapping("/my-posts/{type}/{postId}")
    public ResponseEntity<MyPostDto> getMyPost(
            @PathVariable String type,
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal principal
    ) throws AccessDeniedException {
        return ResponseEntity.ok(userService.getMyPost(principal.getUserId(), postId, type));
    }

    // 내가 쓴 게시글 수정
    @PatchMapping("/my-posts/{type}/{postId}")
    public ResponseEntity<MyPostDto> updatePost(
            @PathVariable String type,
            @PathVariable Long postId,
            @RequestBody String title,
            @RequestBody String content,
            @AuthenticationPrincipal UserPrincipal principal
    ) throws AccessDeniedException {
        MyPostDto dto = userService.updatePost(postId, title, content, type, principal.getUserId());
        return ResponseEntity.ok(dto);
    }

    // 내가 쓴 게시글 삭제
    @DeleteMapping("/my-posts/{type}/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable String type,
            @PathVariable Long postId,
            @AuthenticationPrincipal UserPrincipal principal
    ) throws AccessDeniedException {
        userService.deletePost(postId, type, principal.getUserId());
        return ResponseEntity.ok("게시글 삭제 완료");
    }

    // 내가 쓴 댓글 목록 조회
    @GetMapping("/my-comments")
    public ResponseEntity<List<MyCommentDto>> getMyComments(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getUserId();
        return ResponseEntity.ok(userService.getMyComments(userId));
    }

    // 내가 쓴 특정 댓글 조회
    @GetMapping("/my-comments/{type}/{commentId}")
    public ResponseEntity<MyCommentDto> getMyComment(
            @PathVariable String type,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) throws AccessDeniedException {
        return ResponseEntity.ok(userService.getMyComment(principal.getUserId(), commentId, type));
    }

    // 내가 쓴 댓글 수정
    @PatchMapping("/my-comments/{type}/{commentId}")
    public ResponseEntity<MyCommentDto> updateComment(
            @PathVariable String type,
            @PathVariable Long commentId,
            @RequestBody String content,
            @AuthenticationPrincipal UserPrincipal principal
    ) throws AccessDeniedException {
        MyCommentDto dto = userService.updateComment(commentId, content, type, principal.getUserId());
        return ResponseEntity.ok(dto);
    }

    // 내가 쓴 댓글 삭제
    @DeleteMapping("/my-comments/{type}/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable String type,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal principal
    ) throws AccessDeniedException {
        userService.deleteComment(commentId, type, principal.getUserId());
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    // 프로필 이미지 업로드
    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam MultipartFile image,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userService.uploadProfileImage(principal.getUsername(), image);
        return ResponseEntity.ok("프로필 이미지 업로드 완료");
    }

    // 프로필 이미지 수정
    @PatchMapping("/profile-image")
    public ResponseEntity<String> updateProfileImage(
            @RequestParam MultipartFile image,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userService.updateProfileImage(principal.getUsername(), image);
        return ResponseEntity.ok("프로필 이미지 수정 완료");
    }

    // 프로필 이미지 삭제
    @DeleteMapping("/profile-image")
    public ResponseEntity<String> deleteProfileImage(@AuthenticationPrincipal UserPrincipal principal) {
        userService.deleteProfileImage(principal.getUsername());
        return ResponseEntity.ok("프로필 이미지 삭제 완료");
    }
}
