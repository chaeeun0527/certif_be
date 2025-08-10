package com.example.certif.controller;

import com.example.certif.dto.StudyPostCreateDto;
import com.example.certif.dto.StudyPostResponseDto;
import com.example.certif.dto.StudyPostUpdateDto;
import com.example.certif.service.StudyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyPostController {
    @Autowired
    private StudyPostService studyPostService;

    // 1-1. 스터디 게시판 전체 글 조회 by 카테고리에서 기본 화면 (카테고리 번호=1)
    @GetMapping("/default")
    public ResponseEntity<List<StudyPostResponseDto>> defaultposts() {
        List<StudyPostResponseDto> dtos = studyPostService.getDefaultCategoryPosts();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 1-2. 스터디 게시판 전체 글 조회 by 카테고리
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<StudyPostResponseDto>> posts(@PathVariable Long categoryId){
        List<StudyPostResponseDto> dtos = studyPostService.getPostsByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 1-3. 특정 스터디 게시판 글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<StudyPostResponseDto> getPost(@PathVariable Long postId) {
        StudyPostResponseDto dto = studyPostService.findById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    //  2. 스터디 게시판 글 생성
    @PostMapping
    public ResponseEntity<StudyPostResponseDto> create(@RequestBody StudyPostCreateDto dto,
                                                       @AuthenticationPrincipal User user) {
        StudyPostResponseDto createdDto = studyPostService.create(dto, user);
        return ResponseEntity.status(HttpStatus.OK).body(createdDto);
    }

    // 3. 스터디 게시판 글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<StudyPostResponseDto> update(@PathVariable Long postId,
                                                       @RequestBody StudyPostUpdateDto dto,
                                                       @AuthenticationPrincipal User user){
        StudyPostResponseDto updatedDto = studyPostService.update(postId, dto, user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    // 4. 스터디 게시판 글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal User user) {
        studyPostService.delete(postId, user); // 작성자 확인 포함
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
