package com.example.shyneeds_be.domain.travel_package.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPackageResponseDto {

    @ApiModelProperty(name = "여행 상품 고유번호", required = false, example = "")
    private Long travelPackageId;

    @ApiModelProperty(name = "여행 상품 타이틀", required = false, example = "")
    private String travelPackageTitle;

    @ApiModelProperty(name = "상품 메인 이미지", required = false, example = "")
    private String mainImage;

    @ApiModelProperty(name = "가격", required = false, example = "")
    private String price;

    @ApiModelProperty(name = "상품 키워드", required = false, example = "")
    private String keyword;

    @ApiModelProperty(name = "서브 카테고리 고유번호", required = false, example = "")
    private Long subCategoryId;

    @ApiModelProperty(name = "서브 카테고리 타이틀", required = false, example = "")
    private String subCategoryTitle;
}
