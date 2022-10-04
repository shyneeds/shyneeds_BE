package com.example.shyneeds_be.domain.category.service;

import com.example.shyneeds_be.domain.category.model.entity.Category;
import com.example.shyneeds_be.domain.category.model.entity.SubCategory;
import com.example.shyneeds_be.domain.category.model.entity.ThirdCategory;
import com.example.shyneeds_be.domain.category.model.response.CategoryResponseDto;
import com.example.shyneeds_be.domain.category.model.response.SubCategoryResponseDto;
import com.example.shyneeds_be.domain.category.model.response.ThirdCategoryResponseDto;
import com.example.shyneeds_be.domain.category.repository.CategoryRepository;
import com.example.shyneeds_be.domain.category.repository.SubCategoryRepository;
import com.example.shyneeds_be.domain.category.repository.ThirdCategoryRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ThirdCategoryRepository thirdCategoryRepository;

    public ApiResponseDto<List<CategoryResponseDto>> getCategoryList(){
        try{

            List<CategoryResponseDto> categoryResponseDtoList = categoryRepository.findAll().stream().map(this::response).toList();
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", categoryResponseDtoList);
        } catch(Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());
        }
    }

    private CategoryResponseDto response(Category category){

        // 서브 카테고리
        List<SubCategoryResponseDto> subCategoryResponseDtoList = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(category.getSubCategoryIds(), ",");
        while(st.hasMoreElements()){
            Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(Long.valueOf(st.nextToken()));
            if(optionalSubCategory.isPresent()){
                subCategoryResponseDtoList.add(responseSubCategory(optionalSubCategory.get()));
            }
        }
       return CategoryResponseDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .subCategoryResponseDtoList(subCategoryResponseDtoList)
                .dispFlg(category.isDispFlg())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();

    }

    private SubCategoryResponseDto responseSubCategory(SubCategory subCategory){

        List<ThirdCategoryResponseDto> thirdCategoryResponseDtoList = new ArrayList<>();

        if(subCategory.getThirdCategoryIds() != null) {
            StringTokenizer st = new StringTokenizer(subCategory.getThirdCategoryIds(), ",");
            while (st.hasMoreElements()) {
                Optional<ThirdCategory> optionalThirdCategory = thirdCategoryRepository.findById(Long.valueOf(st.nextToken()));
                if (optionalThirdCategory.isPresent()) {
                    thirdCategoryResponseDtoList.add(responseThirdCategory(optionalThirdCategory.get()));
                }
            }
        }
        return SubCategoryResponseDto.builder()
                .id(subCategory.getId())
                .title(subCategory.getTitle())
                .engTitle(subCategory.getEngTitle())
                .categoryId(subCategory.getCategoryId())
                .thirdCategoryResponseDtoList(thirdCategoryResponseDtoList)
                .dispFlg(subCategory.isDispFlg())
                .createdAt(subCategory.getCreatedAt())
                .updatedAt(subCategory.getUpdatedAt())
                .build();
    }

    private ThirdCategoryResponseDto responseThirdCategory(ThirdCategory thirdCategory) {
        return ThirdCategoryResponseDto.builder()
                .id(thirdCategory.getId())
                .title(thirdCategory.getTitle())
                .subCategoryId(thirdCategory.getSubCategoryId())
                .dispFlg(thirdCategory.isDispFlg())
                .createdAt(thirdCategory.getCreatedAt())
                .updatedAt(thirdCategory.getUpdatedAt())
                .build();
    }
}
