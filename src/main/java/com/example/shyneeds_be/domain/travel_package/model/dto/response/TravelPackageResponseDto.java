package com.example.shyneeds_be.domain.travel_package.model.dto.response;

import com.example.shyneeds_be.domain.category.model.response.CategoryResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPackageResponseDto {


    @ApiModelProperty(value = "고유번호", required = true, example = "")
    private Long id;

    @ApiModelProperty(value = "패키지명", required = true, example = "")
    private String title;

    @ApiModelProperty(value = "카테고리", required = true, example = "")
    private List<CategoryResponseDto> categoryResponseDtoList;
    /*@ApiModelProperty(value = "카테고리 리스트", required = true, example = "")
    private List<Integer> categoryIds;

    @ApiModelProperty(value = "서브 카테고리 리스트", required = false, example = "")
    private List<Integer> subCategoryIds;

    @ApiModelProperty(value = "써드 카테고리 리스트", required = false, example = "")
    private List<Integer> thirdCategoryIds;*/


    @ApiModelProperty(value = "메인 이미지", required = true, example = "1,000,000")
    private String mainImage;

    @ApiModelProperty(value = "상세 이미지", required = true, example = "1,000,000")
    private List<String> descriptionImage;

    @ApiModelProperty(value = "가격", required = true, example = "1,000,000")
    private String price;

    @ApiModelProperty(value = "요약", required = false, example = "9월 대한항공 전세기 직항 이용\n 산토리니 특급 2박, 특급호텔 총 7박\n 모든 것이 포함된 풀패키지 여행")
    private String summary;

    @ApiModelProperty(value = "필수 옵션 이름", required = true, example = "출발일")
    private String requiredOptionName;

    @ApiModelProperty(value = "필수 옵션 값", required = true, example = "2022.12.02(금)출발~2022.12.29(목)도착")
    private String requiredOptionValues;

    @ApiModelProperty(value = "옵션 이름", required = false, example = "싱글차지")
    private String optionalName;

    @ApiModelProperty(value = "옵션 값", required = false, example = "1인 싱글룸 사용시 추가")
    private String optionalValues;

    @ApiModelProperty(value = "항공기 정보", required = false, example = "대한항공 전세기 직항")
    private String flightInfo;

    @ApiModelProperty(value = "재고 여부", required = true, example = "false or 0")
    private boolean soldoutFlg;

    @ApiModelProperty(value = "노출 여부", required = true, example = "true or 1")
    private boolean dispFlg;

    @ApiModelProperty(value = "생성일자", required = true, example = "")
    private Timestamp createdAt;

    @ApiModelProperty(value = "수정일자", required = true, example = "")
    private Timestamp updatedAt;


    @ApiModelProperty(value = "검색 키워드 리스트", required = false, example = "여자끼리,5070끼리")
    private List<String> searchKeyword;

    @ApiModelProperty(value = "삭제 여부", required = true, example = "false or 0")
    private boolean deletedFlg;

    @ApiModelProperty(value = "메인배너 노출 여부", required = true, example = "")
    private boolean mainBannerFlg;

}
