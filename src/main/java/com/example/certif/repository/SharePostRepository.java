package com.example.certif.repository;

import com.example.certif.entity.SharePostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SharePostRepository extends JpaRepository<SharePostEntity, Long> {

    List<SharePostEntity> findByCategoryId(Long categoryId);

}
