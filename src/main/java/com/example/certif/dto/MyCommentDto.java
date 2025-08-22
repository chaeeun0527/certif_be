package com.example.certif.dto;

import com.example.certif.entity.ShareComment;
import com.example.certif.entity.StudyComment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyCommentDto {
    private Long id;
    private String content;
    private String type; // "study" or "share"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long postId;
    private String postTitle;
    private Long categoryId;
    private String categoryName;
    private Long userId;

    // StudyComment 변환
    public static MyCommentDto fromEntity(StudyComment comment, String type) {
        return MyCommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .type(type)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .postId(comment.getPost().getId())
                .postTitle(comment.getPost().getTitle())
                .categoryId(comment.getPost().getCategory().getId())
                .categoryName(comment.getPost().getCategory().getName())
                .userId(comment.getUser().getId())
                .build();
    }

    // ShareComment 변환
    public static MyCommentDto fromEntity(ShareComment comment, String type) {
        return MyCommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .type(type)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .postId(comment.getSharePost().getId())
                .postTitle(comment.getSharePost().getTitle())
                .categoryId(comment.getSharePost().getCategory().getId())
                .categoryName(comment.getSharePost().getCategory().getName())
                .userId(comment.getUser().getId())
                .build();
    }
}
