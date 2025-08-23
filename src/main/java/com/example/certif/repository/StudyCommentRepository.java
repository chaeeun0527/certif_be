package com.example.certif.repository;

import com.example.certif.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    // postId 기준으로 댓글 시간순 조회
    List<StudyComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // userId 기준으로 엔티티 조회
    List<StudyComment> findByUser_Id(Long userId);
}
