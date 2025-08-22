package com.example.certif.dto;

import com.example.certif.entity.ShareComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

public class ShareCommentResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private String nickname;
    private String profileImage;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO  변환
    public ShareCommentResponseDto(ShareComment entity){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.content = entity.getContent();
        this.nickname = entity.getUser().getNickname();
        this.profileImage = entity.getUser().getProfileImage();
        this.postId = entity.getSharePost().getId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();

    }
}
