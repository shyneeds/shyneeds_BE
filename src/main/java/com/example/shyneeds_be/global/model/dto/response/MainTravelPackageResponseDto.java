package com.example.shyneeds_be.global.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainTravelPackageResponseDto {

    @ApiModelProperty(value = "고유번호", required = false, example = "")
    private Long id;

    @ApiModelProperty(value = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "이미지 url", required = false, example = "")
    private String imageUrl; // Main

    @ApiModelProperty(value = "가격", required = false, example = "")
    private String price;

    @ApiModelProperty(value = "요약", required = false, example = "")
    private String summary;

    @ApiModelProperty(value = "태그", required = false, example = "")
    private String tag; // 카테고리 타이틀 => 서브카테고리 타이틀 URL로 변경 필요

//    @ApiModelProperty(value = "태그-그룹별", required = false, example = "")
//    @ApiModelProperty(value = "태그-지역별", required = false, example = "")
//    @ApiModelProperty(value = "태그-테마별", required = false, example = "")
//    @ApiModelProperty(value = "태그-종교별", required = false, example = "")
//    @ApiModelProperty(value = "태그-정치성향별", required = false, example = "")


    @ApiModelProperty(value = "검색키워드", required = false, example = "")
    private String keyword;

}
