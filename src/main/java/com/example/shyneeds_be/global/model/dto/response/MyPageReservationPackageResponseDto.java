package com.example.shyneeds_be.global.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MyPageReservationPackageResponseDto {

    @ApiModelProperty(value = "상품 id", required = false, example = "")
    private Long packageId;

    @ApiModelProperty(value = "이미지 url", required = false, example = "")
    private String imageUrl;

    @ApiModelProperty(value = "상품 타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "옵션 이름", required = false, example = "")
    private String optionName;

    @ApiModelProperty(value = "옵션 내용", required = false, example = "")
    private String optionValue;

    @ApiModelProperty(value = "가격", required = false, example = "")
    private String price;
}
