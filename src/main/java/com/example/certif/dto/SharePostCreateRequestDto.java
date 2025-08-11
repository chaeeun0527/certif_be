package com.example.certif.dto;

//게시물 생성 요청 DTO

import com.example.certif.entity.SharePostEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시물 생성 요청 DTO
@Setter
@Getter
@NoArgsConstructor
public class SharePostCreateRequestDto {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    @NotNull (message = "카테고리 ID는 필수 입력 값입니다.")
    private Long categoryId;

    // DTO -> Entity 변환 메서드
    public SharePostEntity toEntity(Long userId) {
        return SharePostEntity.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .category(category)
                .build();
    }
}
