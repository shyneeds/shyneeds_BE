package com.example.shyneeds_be.domain.travel_package.service;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.GroupPackage;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.GroupPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.repository.GroupPackageRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupPackageService {

    private final GroupPackageRepository groupPackageRepository;

    // 그룹별 여행
    public ApiResponseDto<Map<String, List<GroupPackageResponseDto>>> getGroupPackage(String name) {
        try {

            // Map<SubCategoryTitle, List<GroupPackageResponseDto>>
            Map<String, List<GroupPackageResponseDto>> groupPackageResponseMap = groupPackageRepository.findGroupPackage(name).stream().map(this::responseGroupPackage).toList().stream().collect(Collectors.groupingBy(GroupPackageResponseDto::getSubCategoryTitle));

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", groupPackageResponseMap);
        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());
        }
    }

    // 서브 카테고리 별 여행 리스트
    public ApiResponseDto<List<GroupPackageResponseDto>> getGroupPackageListBySubCategory(String name){
        try{

            List<GroupPackageResponseDto> groupPackageResponseDtoList = groupPackageRepository.findBySubTitle(name).stream().map(this::responseGroupPackage).toList();
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", groupPackageResponseDtoList);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다." + e.getMessage());
        }
    }

    private GroupPackageResponseDto responseGroupPackage(GroupPackage groupPackage) {

        String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/" +
                groupPackage.getTravelTitle();
        String mainImage = imageDir + "/main/" + groupPackage.getMainImage();
        return GroupPackageResponseDto.builder()
                .travelPackageId(groupPackage.getTravelId())
                .travelPackageTitle(groupPackage.getTravelTitle())
                .mainImage(mainImage)
                .keyword(groupPackage.getSearchKeyword())
                .subCategoryId(groupPackage.getSubCategoryId())
                .subCategoryTitle(groupPackage.getSubCategoryTitle())
                .build();
    }
}
