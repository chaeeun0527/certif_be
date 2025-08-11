package com.example.certif.controller;

import com.example.certif.dto.StudyCommentDto;
import com.example.certif.entity.User;
import com.example.certif.service.StudyCommentService;
import com.example.certif.service.StudyPostService;
import com.example.certif.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class StudyCommentController {

    private final JwtUtil jwtUtil;

    @Autowired
    private StudyCommentService studyCommentService;

    @Autowired
    private StudyPostService studyPostService;

    // 1. 스터디 게시판 글의 댓글 조회
    @GetMapping("api/study/{postId}/comments")
    public ResponseEntity<List<StudyCommentDto> getComments(@PathVariable Long postId) {
        // 서비스에 위임
        List<StudyCommentDto> dtos = studyCommentService.comments(postId);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }


    // 2. 스터디 게시판 글의 댓글 생성하기
    @PostMapping("/api/study/{postId}/comments")
    public ResponseEntity<StudyCommentDto> create(@PathVariable Long postId,
                                                  @RequestBody StudyCommentDto dto,
                                                  @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        // 서비스에 위임
        StudyCommentDto createdDto = studyCommentService.create(postId, dto, userId);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(createdDto);
    }


    // 3. 댓글 수정
    @PatchMapping("/api/comments/{commentId}")
    public ResponseEntity<StudyCommentDto> update(@PathVariable Long commentId,
                                                  @RequestBody StudyCommentDto dto,
                                                  @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        // 서비스에 위임
        StudyCommentDto updatedDto = studyCommentService.update(commentId, dto, userId);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);

    }

    // 4. 댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId,
                                       @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        // 서비스에 위임
        studyCommentService.delete(commentId, userId);
        // 결과 응답
        return ResponseEntity.noContent().build();
    }
}
