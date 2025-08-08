package com.example.certif.service;

import com.example.certif.dto.FavoriteCertificateDto;
import com.example.certif.entity.Favorite;
import com.example.certif.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    // 1. 사용자의 즐겨찾기 자격증 목록 조회
    public List<FavoriteCertificateDto> getFavoritesByUser(User user) {
        List<Favorite> favorites = favoriteRepository.findByUser(user);
        return favorites.stream()
                .map(fav -> new FavoriteCertificateDto(
                        fav.getCertificate().getId(),
                        fav.getCertificate().getName()))
                .collect(Collectors.toList());
    }
}
