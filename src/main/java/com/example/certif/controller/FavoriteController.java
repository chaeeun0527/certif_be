package com.example.certif.controller;

import com.example.certif.dto.CertificateDto;
import com.example.certif.dto.FavoriteCertificateDto;
import com.example.certif.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    // 1. 사용자의 즐겨찾기 자격증 목록 조회
    @GetMapping("/api/user/favorites")
    public ResponseEntity<List<FavoriteCertificateDto>> getFavorites(@AuthenticationPrincipal User user){
        List<FavoriteCertificateDto> dtos = favoriteService.getFavoritesByUser(user);
        ResponseEntity.status(HttpStatus.OK).body(dtos);

    }

    // 2. 자격증을 즐겨찾기에 등록


    // 3. 자격증을 즐겨찾기에서 해제
}
