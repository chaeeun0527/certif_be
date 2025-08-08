package com.example.certif.controller;

import com.example.certif.dto.CertificateDetailDto;
import com.example.certif.dto.CertificateDto;
import com.example.certif.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    // 1. 카테고리별 자격증 목록 조회
    @GetMapping("/api/categories/{categoryId}/certificates")
    public ResponseEntity<List<CertificateDto>> getCertificates(@PathVariable Long categoryId){
        List<CertificateDto> dtos = certificateService.getCertificates(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 2. 자격증 상세 정보 조회
    @GetMapping("/api/certificates/{certificateId}")
    public ResponseEntity<CertificateDetailDto> getCertificateDetail(@PathVariable Long certificateId){
        CertificateDetailDto dto = certificateService.getCertificateDetail(certificateId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


}
