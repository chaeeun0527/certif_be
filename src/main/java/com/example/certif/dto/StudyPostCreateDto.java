package com.example.certif.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class StudyPostCreateDto {
    private String title;
    private String content;
    private Long categoryId; // 등록 시 카테고리 번호 필요
}
