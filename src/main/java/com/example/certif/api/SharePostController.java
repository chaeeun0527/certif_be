package com.example.certif.api;

import com.example.certif.dto.*;
import com.example.certif.entity.SharePostEntity;
import com.example.certif.service.ShareService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

    // 임시 사용자 ID
    private static final Long loggedInUserId = 1L;

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
    public ResponseEntity<SharePostResponseDto> createPost(@RequestBody @Valid SharePostCreateRequestDto requestDto) {
        SharePostResponseDto newPost = shareService.createPost(requestDto, loggedInUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);

    }

    //게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<SharePostResponseDto> updatePost(
            @PathVariable Long postId, @RequestBody @Valid SharePostUpdateRequestDto requestDto) {
        SharePostResponseDto updatedPost = shareService.updatePost(postId, requestDto, loggedInUserId);
        return ResponseEntity.ok(updatedPost);
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        shareService.deletePost(postId, loggedInUserId);
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
            @RequestBody @Valid ShareCommentCreateRequestDto requestDto){
        ShareCommentResponseDto newComment = shareService.createComment(postId, requestDto, loggedInUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);

    }
    // 댓글 수정
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ShareCommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid ShareCommentUpdateRequestDto requestDto ){
        ShareCommentResponseDto updatedComment = shareService.updateComment(commentId, requestDto, loggedInUserId);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommnet(
            @PathVariable Long postId,
            @PathVariable Long commentId){
        shareService.deleteComment(commentId, loggedInUserId);
        return ResponseEntity.noContent().build();
    }

}

    //e



