package com.example.certif.dto;

import com.example.certif.entity.ShareCommentEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

public class ShareCommentResponseDto {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO  변환
    public ShareCommentResponseDto(ShareCommentEntity entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.userId = entity.getUserId();
        this.postId = entity.getSharePost().getId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();

    }
}
