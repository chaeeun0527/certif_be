package com.example.certif.controller;

import com.example.certif.dto.FavoriteCertificateDto;
import com.example.certif.entity.Favorite;
import com.example.certif.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    // 1. 사용자의 즐겨찾기 자격증 목록 조회
    @GetMapping
    public ResponseEntity<List<FavoriteCertificateDto>> getFavorites(@AuthenticationPrincipal User user){

        List<FavoriteCertificateDto> dtos = favoriteService.getFavoritesByUser(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);

    }


    // 2. 자격증을 즐겨찾기에 등록
    @PostMapping("/{certificateId}")
    public ResponseEntity<String> addFavorite(@RequestParam Long certificateId,
                                              @AuthenticationPrincipal User user) {
        favoriteService.addFavorite(user.getId(), certificateId);
        return ResponseEntity.status(HttpStatus.OK).body("즐겨찾기에 추가되었습니다.");
    }

    // 3. 자격증을 즐겨찾기에서 해제
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<String> removeFavorite(@RequestParam Long certificateId,
                                                 @AuthenticationPrincipal User user){
        favoriteService.removeFavorite(user.getId(), certificateId);
        return ResponseEntity.status(HttpStatus.OK).body("즐겨찾기가 해제되었습니다.");

    }
}
