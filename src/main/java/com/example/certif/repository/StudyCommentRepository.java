package com.example.certif.repository;

import com.example.certif.entity.StudyComment;
import com.example.certif.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    // postId 기준으로 댓글 시간순 조회
    List<StudyComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    @Query("SELECT sc FROM StudyComment sc " +
            "JOIN sc.user u " +
            "JOIN FETCH sc.post p " +
            "JOIN FETCH p.category c " +
            "WHERE u.id = :userId")
    List<StudyComment> findByUserIdWithPostAndCategory(@Param("userId") Long userId);

}
