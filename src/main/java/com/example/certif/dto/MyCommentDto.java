package com.example.certif.dto;

import com.example.certif.entity.ShareComment;
import com.example.certif.entity.SharePost;
import com.example.certif.entity.StudyComment;
import com.example.certif.entity.StudyPost;
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

    // StudyComment 변환
    public static MyCommentDto fromEntity(StudyComment comment, String type) {
        return MyCommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .type(type)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
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
                .build();
    }
}
