package com.example.certif.controller;

import com.example.certif.entity.Category;
import com.example.certif.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    // 1. 자격증 카테고리 목록 조회
    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    // 2. 특정 자격증 카테고리 조회
    @GetMapping("/{categoryId}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}
