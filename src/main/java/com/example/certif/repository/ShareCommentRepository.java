package com.example.certif.repository;

import com.example.certif.entity.ShareCommentEntity;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareCommentRepository extends JpaRepository<ShareCommentEntity, Long> {

    //게시물 ID로 댓글 목록 조회
    List<ShareCommentEntity> findBySharePostId(Long postId);
}
