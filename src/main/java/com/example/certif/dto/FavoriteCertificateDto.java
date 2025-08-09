package com.example.certif.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCertificateDto {
    private Long certificateId;
    private String certificateName;
    // 카테고리 추가
    private Long categoryId;
    private String categoryName;
}
