package com.example.certif.repository;

import com.example.certif.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    // postId 기준으로 댓글 시간순 조회
    List<StudyComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // 특정 사용자가 작성한 댓글 목록 조회
    List<StudyComment> findByUser_Id(Long userId);
}
