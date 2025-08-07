package com.example.certif.repository;

import com.example.certif.entity.SharePostEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface SharePostRepository extends CrudRepository<SharePostEntity, Long> {

    @Override
    ArrayList<SharePostEntity> findAll();
}
