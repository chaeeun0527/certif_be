package com.example.certif.service;

import com.example.certif.dto.StudyPostCreateDto;
import com.example.certif.dto.StudyPostResponseDto;
import com.example.certif.dto.StudyPostUpdateDto;
import com.example.certif.entity.Category;
import com.example.certif.entity.StudyPost;
import com.example.certif.repository.CategoryRepository;
import com.example.certif.repository.StudyPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;



import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    @Autowired
    private StudyPostRepository studyPostRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    // 1-1. 스터디 게시판 전체 글 조회 by 카테고리에서 기본 화면 (카테고리 번호=1)
    public List<StudyPostResponseDto> getDefaultCategoryPosts() {
        List<StudyPost> posts = studyPostRepository.findByCategoryIdOrderByCreatedAtDesc(1);
        List<StudyPostResponseDto> dtos = new ArrayList<>();
        for (StudyPost p : posts) {
            dtos.add(StudyPostResponseDto.fromEntity(p));
        }
        return dtos;
    }

    // 1-2. 스터디 게시판 전체 글 조회 by 카테고리
    public List<StudyPostResponseDto> getPostsByCategory(Long categoryId) {
        List<StudyPost> posts = studyPostRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
        List<StudyPostResponseDto> dtos = new ArrayList<>();
        for (StudyPost p : posts) {
            dtos.add(StudyPostResponseDto.fromEntity(p));
        }
        return dtos;
    }

    // 1-3. 특정 스터디 게시판 글 조회
    public StudyPostResponseDto findById(Long postId) {
        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));
        return StudyPostResponseDto.fromEntity(post);
    }

    // 2. 스터디 게시판 글 생성
    @Transactional
    public StudyPostResponseDto create(StudyPostCreateDto dto, User user) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리"));
        StudyPost studypost = StudyPost.createStudyPost(dto, user, category);
        StudyPost created = studyPostRepository.save(studypost);
        return StudyPostResponseDto.fromEntity(created);
    }

    // 3. 스터디 게시판 글 수정
    @Transactional
    public StudyPostResponseDto update(Long postId, StudyPostUpdateDto dto, User user) {
        StudyPost target = studyPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("스터디 게시판 글 수정 실패! 대상 게시판 글이 없습니다."));
        if (!target.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        target.patch(dto);
        StudyPost updated = studyPostRepository.save(target);
        return StudyPostResponseDto.fromEntity(updated);
    }

    // 4. 스터디 게시판 글 삭제
    @Transactional
    public void delete(Long postId, User user) {
        StudyPost target = studyPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시판 글 삭제 실패! 대상이 없습니다."));
        if (!target.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        studyPostRepository.delete(target);
    }
}
