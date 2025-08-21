package com.example.certif.repository;

import com.example.certif.dto.MyCommentDto;
import com.example.certif.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    // postId 기준으로 댓글 시간순 조회
    List<StudyComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // MyCommentDto에 postId, postTitle 추가
    @Query("SELECT new com.example.certif.dto.MyCommentDto(" +
            "c.id, c.content, 'study', c.createdAt, c.updatedAt, c.post.id, c.post.title) " +
            "FROM StudyComment c " +
            "JOIN c.user u " +
            "WHERE u.email = :email")
    List<MyCommentDto> findMyCommentsByUserEmail(@Param("email") String email);
}
