package com.example.certif.service;

import com.example.certif.entity.Category;
import com.example.certif.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    // 1. 카테고리 목록
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 2. 특정 카테고리 조회
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. ID=" + id));
    }
}
