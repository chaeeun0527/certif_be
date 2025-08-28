package com.example.certif.dto;

// 댓글 수정 요청 DTO

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShareCommentUpdateRequestDto {
    private String content;
}
