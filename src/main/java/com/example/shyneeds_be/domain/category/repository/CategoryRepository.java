package com.example.shyneeds_be.domain.category.repository;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
