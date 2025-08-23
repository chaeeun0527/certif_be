package com.example.certif.repository;

import com.example.certif.entity.SharePost;
import com.example.certif.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharePostRepository extends JpaRepository<SharePost, Long> {

    List<SharePost> findByCategoryId(Long categoryId);

    @Query("SELECT c FROM SharePost c WHERE c.user.id = :userId")
    List<SharePost> findByUserIdSimple(@Param("userId") Long userId);
}
