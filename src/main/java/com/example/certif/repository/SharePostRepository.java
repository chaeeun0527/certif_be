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

    // UserId 기준으로 엔티티 조회
    List<SharePost> findByUserId(Long userId);
}
