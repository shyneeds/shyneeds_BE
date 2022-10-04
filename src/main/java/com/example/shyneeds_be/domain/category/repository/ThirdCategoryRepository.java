package com.example.shyneeds_be.domain.category.repository;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import com.example.shyneeds_be.domain.category.model.entity.ThirdCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdCategoryRepository extends JpaRepository<ThirdCategory, Long> {
}
