package com.example.certif.dto;

import com.example.certif.entity.ShareCommentEntity;
import com.example.certif.entity.SharePostEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShareCommentCreateRequestDto {
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    //DTO -> Entity 변환
    public ShareCommentEntity toEntity(Long userId, SharePostEntity sharePost){
        return ShareCommentEntity.builder()
                .content(this.content)
                .userId(userId)
                .sharePost(sharePost)
                .build();
    }
}
