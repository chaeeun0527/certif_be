package com.example.certif.repository;

import com.example.certif.dto.MyPostDto;
import com.example.certif.entity.SharePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharePostRepository extends JpaRepository<SharePost, Long> {

    List<SharePost> findByCategoryId(Long categoryId);

    @Query("SELECT new com.example.certif.dto.MyPostDto(p.id, p.title, p.content, 'share', p.createdAt, p.updatedAt) " +
            "FROM SharePost p " +
            "JOIN p.user u " +
            "WHERE u.email = :email")
    List<MyPostDto> findMyPostsByUserEmail(@Param("email") String email);
}
