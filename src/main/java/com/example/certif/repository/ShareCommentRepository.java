package com.example.certif.repository;

import com.example.certif.entity.ShareComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareCommentRepository extends JpaRepository<ShareComment, Long> {

    //게시물 ID로 댓글 목록 조회
    List<ShareComment> findBySharePostId(Long postId);

    @Query("SELECT c FROM ShareComment c WHERE c.user.id = :userId")
    List<ShareComment> findByUserIdSimple(@Param("userId") Long userId);

}
