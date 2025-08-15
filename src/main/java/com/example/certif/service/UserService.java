package com.example.certif.service;

import com.example.certif.entity.User;
import com.example.certif.repository.CommentRepository;
import com.example.certif.repository.PostRepository;
import com.example.certif.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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

    public void uploadProfileImage(String email, MultipartFile image) {
        // 이미지 저장 후 사용자 프로필 업데이트
    }

    public void updateProfileImage(String email, MultipartFile image) {
        // 기존 이미지 제거 후 새로운 이미지로 변경
    }

    public void deleteProfileImage(String email) {
        // 프로필 이미지 제거
    }
}
