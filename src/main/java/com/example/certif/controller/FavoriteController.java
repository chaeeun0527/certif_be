package com.example.certif.controller;

import com.example.certif.dto.FavoriteCertificateDto;
import com.example.certif.security.UserPrincipal;
import com.example.certif.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 1. 사용자의 즐겨찾기 자격증 목록 조회
    @GetMapping
    public ResponseEntity<List<FavoriteCertificateDto>> getFavorites(
            @AuthenticationPrincipal UserPrincipal principal
    ){
        Long userId = principal.getUserId();

        List<FavoriteCertificateDto> dtos = favoriteService.getFavoritesByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);

    }


    // 2. 자격증을 즐겨찾기에 등록
    @PostMapping("/{certificateId}")
    public ResponseEntity<String> addFavorite(
            @PathVariable Long certificateId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = principal.getUserId();
        favoriteService.addFavorite(userId, certificateId);
        return ResponseEntity.status(HttpStatus.OK).body("즐겨찾기에 추가되었습니다.");
    }

    // 3. 자격증을 즐겨찾기에서 해제
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<String> removeFavorite(
            @PathVariable Long certificateId,
            @AuthenticationPrincipal UserPrincipal principal
    ){
        Long userId = principal.getUserId();
        favoriteService.removeFavorite(userId, certificateId);
        return ResponseEntity.status(HttpStatus.OK).body("즐겨찾기가 해제되었습니다.");

    }
}
