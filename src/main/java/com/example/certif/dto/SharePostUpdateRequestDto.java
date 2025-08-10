package com.example.certif.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시물 수정 요청 DTO

@Getter
@Setter
@NoArgsConstructor
public class SharePostUpdateRequestDto {
    private String title;
    private String content;
    private Long categoryId;
}