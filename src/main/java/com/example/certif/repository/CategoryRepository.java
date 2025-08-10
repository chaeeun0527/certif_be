package com.example.certif.repository;

import com.example.certif.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {


}
