package com.example.certif.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDetailDto {
    private Long certificateId;
    private String certificateName;
    private Long categoryId;
    private String categoryName;
    private String overview;
    private String officialSite;
    private String organization;
    private String examMethod;
    private String passingCriteria;
    private String qualification;
    private String fee;
    private String features;

    private boolean favorited; // 로그인 사용자 기준 즐겨찾기 여부
}
