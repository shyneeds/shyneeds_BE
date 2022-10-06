package com.example.shyneeds_be.global.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainResponseDto {


    @ApiModelProperty(value = "메인 베너 리스트", required = false, example = "")
    private List<MainBannerResponseDto> mainBannerList;

//    @ApiModelProperty(value = "큐레이션 카테고리", required = false, example = "")

    @ApiModelProperty(value = "카테고리 별 상품 리스트", required = false, example = "")
    private Map<String, List<MainTravelPackageResponseDto>> mainCategoryPackageList;


    @ApiModelProperty(value = "후기", required = false, example = "")
    private List<String> bestReviewList;
}
