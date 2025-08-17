package com.example.certif.service;

import com.example.certif.dto.*;
import com.example.certif.entity.Category;
import com.example.certif.entity.ShareCommentEntity;
import com.example.certif.entity.SharePostEntity;
import com.example.certif.entity.User;
import com.example.certif.repository.CategoryRepository;
import com.example.certif.repository.ShareCommentRepository;
import com.example.certif.repository.SharePostRepository;
import com.example.certif.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShareService {

    @Autowired
    private SharePostRepository sharePostRepository;

    @Autowired
    private ShareCommentRepository shareCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 게시물 관련 서비스

    // 게시물 - 1. 글 목록 조회 (첫번째 카테고리 페이지)
    @Transactional(readOnly = true)
    public List<SharePostResponseDto> findFirstCategoryPosts() {
        // 첫번째 카테고리 ID 가정
        Long firstCategoryId = 1L;
        List<SharePostEntity> entities = sharePostRepository.findByCategoryId(firstCategoryId);
        return entities.stream()
                .map(SharePostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시물 - 2. 카테고리별 게시물 목록 조회
    @Transactional(readOnly = true)
    public List<SharePostResponseDto> findPostsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        List<SharePostEntity> entities = sharePostRepository.findByCategoryId(categoryId);
        return entities.stream()
                .map(SharePostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시물 - 3. 특정 글 내용 조회
    @Transactional(readOnly = true)
    public SharePostResponseDto findPostById(Long postId){
        SharePostEntity entity = sharePostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));
        return new SharePostResponseDto(entity);
    }

    // 게시물 - 4. 게시물 등록
    @Transactional
    public SharePostResponseDto createPost(SharePostCreateRequestDto dto, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()-> new IllegalArgumentException("카테고리를 찾을 수 없습니다"));

        SharePostEntity newPost = dto.toEntity(user, category);
        SharePostEntity savedPost = sharePostRepository.save(newPost);
        return new SharePostResponseDto(savedPost);
    }

    // 게시물 - 5. 게시물 수정
    @Transactional
    public SharePostResponseDto updatePost(Long postId, SharePostUpdateRequestDto dto, Long userId){
        SharePostEntity target = sharePostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

        //작성자 확인
        if(!target.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다"));

        target.update(dto.getTitle(), dto.getContent(), category);
        SharePostEntity updatedPost = sharePostRepository.save(target);
        return new SharePostResponseDto(updatedPost);

    }
    // 게시물 - 6. 게시물 삭제
    @Transactional
    public void deletePost(Long postId, Long userId){
        SharePostEntity target = sharePostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

        //작성자 확인
        if(!target.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        sharePostRepository.delete(target);
    }

//    댓글 관련 서비스

    // 댓글 - 1. 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<ShareCommentResponseDto> findCommentsByPostId(Long postId){
        List<ShareCommentEntity> entities = shareCommentRepository.findBySharePostId(postId);
        return entities.stream()
                .map(ShareCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 - 2. 댓글 등록
    @Transactional
    public ShareCommentResponseDto createComment(Long postId, ShareCommentCreateRequestDto dto, Long userId){
        SharePostEntity sharePost = sharePostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + postId));

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        ShareCommentEntity newComment = dto.toEntity(user, sharePost);
        ShareCommentEntity savedComment = shareCommentRepository.save(newComment);
        return new ShareCommentResponseDto(savedComment);
    }

    // 댓글 - 3. 댓글 수정
    @Transactional
    public ShareCommentResponseDto updateComment(Long commentId, ShareCommentUpdateRequestDto dto, Long userId){
        ShareCommentEntity target = shareCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다.: " + commentId));

        //작성자 확인
        if(!target.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        target.update(dto.getContent());
        ShareCommentEntity updatedCommnet = shareCommentRepository.save(target);
        return new ShareCommentResponseDto(updatedCommnet);


    }

    // 댓글 - 4. 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId){
        ShareCommentEntity target = shareCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        //작성자 확인
        if(!target.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        shareCommentRepository.delete(target);
    }
}