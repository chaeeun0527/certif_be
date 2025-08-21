package com.example.certif.repository;

import com.example.certif.dto.MyPostDto;
import com.example.certif.entity.StudyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {

    // 카테고리별 최신순 정렬
    List<StudyPost> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);

    // 게시글 ID로 조회
    Optional<StudyPost> findById(Long id);

    // DTO로 바로 조회
    @Query("SELECT new com.example.certif.dto.MyPostDto(p.id, p.title, p.content, 'study', p.createdAt, p.updatedAt) " +
            "FROM StudyPost p " +
            "JOIN p.user u " +
            "WHERE u.email = :email")
    List<MyPostDto> findMyPostsByUserEmail(@Param("email") String email);
}
