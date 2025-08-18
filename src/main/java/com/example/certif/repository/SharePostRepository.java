package com.example.certif.repository;

import com.example.certif.entity.SharePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SharePostRepository extends JpaRepository<SharePost, Long> {

    List<SharePost> findByCategoryId(Long categoryId);

    List<SharePost> findByUserEmail(String email);
}
