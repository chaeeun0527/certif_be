package com.example.certif.controller;

import com.example.certif.dto.*;
import com.example.certif.service.ShareService;
import com.example.certif.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/share")

public class SharePostController {

    @Autowired
    private ShareService shareService;

    @Autowired
    private JwtUtil jwtUtil;

    // JWT 토큰에서 userId 추출 메서드
    private Long getUserIdFromRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)){
                return jwtUtil.getUserIdFromToken(token);
            }
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    //글 전체 목록 조회 - 첫번째 카테고리
    @GetMapping("/default")
    public ResponseEntity<List<SharePostResponseDto>> getDefaultPosts() {
        List<SharePostResponseDto> posts = shareService.findFirstCategoryPosts();
        return ResponseEntity.ok(posts);
    }

    //카테고리별 게시물 목록 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<List<SharePostResponseDto>> getPostsByCategory(@PathVariable Long categoryId) {
        List<SharePostResponseDto> posts = shareService.findPostsByCategoryId(categoryId);
        return ResponseEntity.ok(posts);
    }

    //특정 글 내용 조회
    @GetMapping("/{postId}")
    public ResponseEntity<SharePostResponseDto> getPostById(@PathVariable Long postId) {
        SharePostResponseDto post = shareService.findPostById(postId);
        return ResponseEntity.ok(post);

    }

    // 게시물 등록
    @PostMapping
    public ResponseEntity<SharePostResponseDto> createPost(@RequestBody @Valid SharePostCreateRequestDto requestDto, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        SharePostResponseDto newPost = shareService.createPost(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);

    }

    //게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<SharePostResponseDto> updatePost(
            @PathVariable Long postId, @RequestBody @Valid SharePostUpdateRequestDto requestDto, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        SharePostResponseDto updatedPost = shareService.updatePost(postId, requestDto, userId);
        return ResponseEntity.ok(updatedPost);
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        shareService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    //댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<ShareCommentResponseDto>> getCommentsByPostId(@PathVariable Long postId){
        List<ShareCommentResponseDto> comments = shareService.findCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    //댓글 등록
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ShareCommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid ShareCommentCreateRequestDto requestDto, HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        ShareCommentResponseDto newComment = shareService.createComment(postId, requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);

    }
    // 댓글 수정
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ShareCommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid ShareCommentUpdateRequestDto requestDto,
            HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        ShareCommentResponseDto updatedComment = shareService.updateComment(commentId, requestDto, userId);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommnet(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        shareService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

}





