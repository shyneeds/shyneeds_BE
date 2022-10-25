package com.example.shyneeds_be.domain.category.repository;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import com.example.shyneeds_be.domain.category.model.entity.CategoryTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query(value = "SELECT CA.title AS title, SCA.title AS subCategoryTitle FROM shyneeds.category AS CA " +
            "INNER JOIN shyneeds.sub_category AS SCA ON CA.id = SCA.category_id " +
            "WHERE CA.disp_flg = 1 AND SCA.disp_flg = 1"
    ,nativeQuery = true)
    List<CategoryTitle> getCategoryTitleList();
}
