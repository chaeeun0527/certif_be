package com.example.certif.repository;

import com.example.certif.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    // 특정 카테고리 ID에 해당하는 자격증 목록 조회
    List<Certificate> findByCategoryId(Long categoryId);
}
