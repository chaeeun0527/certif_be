package com.example.certif.service;

import com.example.certif.dto.StudyCommentDto;
import com.example.certif.entity.StudyComment;
import com.example.certif.entity.StudyPost;
import com.example.certif.repository.StudyCommentRepository;
import com.example.certif.repository.StudyPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class StudyCommentService {

    private StudyCommentRepository studyCommentRepository;
    private StudyPostRepository studyPostRepository;
    private UserRepository userRepository;


    // 1. 스터디 게시판 글의 댓글 조회하기
    public List<StudyCommentResponseDto> comments(Long postId) {
        // 1. 댓글 조회
        List<StudyComment> comments = studyCommentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        // 2. 엔티티 -> dto 변환
        List<StudyCommentResponseDto> dtos = new ArrayList<StudyCommentResponseDto>();
        for (int i = 0; i < comments.size(); i++) {
            StudyComment c = comments.get(i);
            StudyCommentResponseDto dto = StudyCommentResponseDto.createStudyCommentDto(c);
            dtos.add(dto);
        }
        // 3. 결과 반환
        return dtos;
    }


    // 2. 스터디 게시판 글의 댓글 생성하기
    @Transactional
    public StudyCommentDto create(Long postId, StudyCommentDto dto, User user) {
        // 1. 게시글 조회 및 예외 발생
        StudyPost studyPost = studyPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! " +
                        "대상 게시글이 존재하지 않습니다."));
        // 2. 댓글 엔티티 생성
        StudyComment studyComment = StudyComment.createComment(dto, studyPost, user);
        // 3. 댓글 엔티티를 db에 저장
        StudyComment created = studyCommentRepository.save(studyComment);
        // 4. dto로 변환하여 반환
        return StudyCommentDto.createStudyCommentDto(created);
    }


    // 3. 스터디 게시판 글의 댓글 수정하기
    @Transactional
    public StudyCommentDto update(Long commentId, StudyCommentDto dto, User user) {
        // 1. 댓글 조회 및 예외 발생
        StudyComment target = studyCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패!" +
                        "대상 댓글이 없습니다."));
        // 2. 권한 체크
        if (!target.getUser().getId().equals(user.getId())) {
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }
        // 3. 댓글 수정
        target.patch(dto);
        // 4. db로 갱신
        StudyComment updated = studyCommentRepository.save(target);
        // 5. 댓글 엔티티를 dto로 변환 및 반환
        return StudyCommentDto.createStudyCommentDto(updated);
    }


    // 4. 스터디 게시판 글의 댓글 삭제하기
    @Transactional
    public void delete(Long commentId, User user) {
        // 1. 댓글 조회 및 예외 발생
        StudyComment target = studyCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상 댓글이 없습니다."));
        // 2. 권한 확인: 댓글 작성자와 요청한 사용자가 일치하는지 확인
        if (!target.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 삭제 실패! 권한이 없습니다.");
        }
        // 3. 댓글 삭제
        studyCommentRepository.delete(target);
    }




}
