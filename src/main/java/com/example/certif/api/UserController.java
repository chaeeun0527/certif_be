package com.example.certif.api;

import com.example.certif.dto.*;
import com.example.certif.entity.User;
import com.example.certif.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/my-page")
    public ResponseEntity<User> getMyPage(@RequestParam String email) {
        return ResponseEntity.ok(userService.getMyInfo(email));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetTokenRequest request) {
        userService.sendPasswordResetToken(request.getEmail());
        return ResponseEntity.ok("비밀번호 재설정 이메일 전송 완료");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다");
    }

    @GetMapping("/my-comments")
    public ResponseEntity<List<String>> getMyComments(@RequestParam String email) {
        return ResponseEntity.ok(userService.getMyComments(email));
    }

    @PatchMapping("/my-comments/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Long id, @RequestParam String content) {
        userService.updateComment(id, content);
        return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/my-comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        userService.deleteComment(commentId);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<String>> getMyPosts(@RequestParam String email) {
        return ResponseEntity.ok(userService.getMyPosts(email));
    }

    @PatchMapping("/my-posts/{id}")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestParam String content) {
        userService.updatePost(id, content);
        return ResponseEntity.ok("게시글 수정 완료");
    }

    @DeleteMapping("/my-posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        userService.deletePost(postId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }

    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam MultipartFile image, @RequestParam String email) {
        userService.uploadProfileImage(email, image);
        return ResponseEntity.ok("프로필 이미지 업로드 완료");
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<String> updateProfileImage(@RequestParam MultipartFile image, @RequestParam String email) {
        userService.updateProfileImage(email, image);
        return ResponseEntity.ok("프로필 이미지 수정 완료");
    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<String> deleteProfileImage(@RequestParam String email) {
        userService.deleteProfileImage(email);
        return ResponseEntity.ok("프로필 이미지 삭제 완료");
    }
}
