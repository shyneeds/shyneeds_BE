package com.example.shyneeds_be.domain.category.repository;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import com.example.shyneeds_be.domain.category.model.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Query(value = "SELECT * FROM shyneeds.sub_category " +
            "WHERE category_id = :categoryId"
            ,nativeQuery = true)
    List<SubCategory> findByCategoryId(@Param("categoryId") Long categoryId);
}
