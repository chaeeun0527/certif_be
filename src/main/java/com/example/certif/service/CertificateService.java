package com.example.certif.service;

import com.example.certif.dto.CertificateDetailDto;
import com.example.certif.dto.CertificateDto;
import com.example.certif.entity.Certificate;
import com.example.certif.repository.CertificateRepository;
import com.example.certif.repository.FavoriteRepository;
import com.example.certif.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final FavoriteRepository favoriteRepository;

    // 1. 카테고리별 자격증 목록 조회
    public List<CertificateDto> getCertificates(Long categoryId) {
        List<Certificate> certificates = certificateRepository.findByCategoryId(categoryId);
        return certificates.stream()
                .map(c -> new CertificateDto(
                        c.getId(),
                        c.getName()
                ))
                .collect(Collectors.toList());
    }

    // 2. 자격증 상세 정보 조회
    public CertificateDetailDto getCertificateDetail(Long certificateId) {
        Certificate c = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 자격증이 없습니다. ID=" + certificateId));

        boolean favorited = false;

        // 로그인되어 있는 경우 즐겨찾기 여부 체크
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            Long userId = userPrincipal.getUserId();
            favorited = favoriteRepository.existsByUserIdAndCertificateId(userId, certificateId);
        }

        return new CertificateDetailDto(
                c.getId(),
                c.getName(),
                c.getCategory().getId(),
                c.getCategory().getName(),
                c.getOverview(),
                c.getOfficialSite(),
                c.getOrganization(),
                c.getExamMethod(),
                c.getPassingCriteria(),
                c.getQualification(),
                c.getFee(),
                c.getFeatures(),
                favorited
        );

    }
}
