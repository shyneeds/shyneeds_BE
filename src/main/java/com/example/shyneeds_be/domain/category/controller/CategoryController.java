package com.example.shyneeds_be.domain.category.controller;

import com.example.shyneeds_be.domain.category.model.response.CategoryResponseDto;
import com.example.shyneeds_be.domain.category.model.response.SubCategoryResponseDto;
import com.example.shyneeds_be.domain.category.service.CategoryService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "전체 카테고리 리스트")
    @GetMapping("")
    public ApiResponseDto<List<CategoryResponseDto>> getCategoryList(){
        return categoryService.getCategoryList();
    }

    @ApiOperation(value = "메인 카테고리의 서브 카테고리 조회")
    @GetMapping("/{id}")
    public ApiResponseDto<List<SubCategoryResponseDto>> getSubCategory(@PathVariable Long id){
        return categoryService.getSubCategory(id);
    }

}
