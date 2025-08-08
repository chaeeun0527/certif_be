package com.example.certif.repository;

import com.example.certif.entity.Certificate;
import com.example.certif.entity.Favorite;
import com.example.certif.entity.id.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    // 미리 만들어놓음
    List<Favorite> findByUser(User user);
    boolean existsByUserAndCertificate(User user, Certificate certificate);
    void deleteByUserAndCertificate(User user, Certificate certificate);
}
