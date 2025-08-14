package com.example.certif.dto;

import com.example.certif.entity.Category;
import com.example.certif.entity.ShareCommentEntity;
import com.example.certif.entity.SharePostEntity;
import com.example.certif.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShareCommentCreateRequestDto {
    @NotBlank(message = "내용은 필수 입력값 입니다.")
    private String content;

    //DTO -> Entity 변환
    public ShareCommentEntity toEntity(User user, SharePostEntity sharePost){
        return ShareCommentEntity.builder()
                .content(this.content)
                .user(user)
                .sharePost(sharePost)
                .build();
    }
}
