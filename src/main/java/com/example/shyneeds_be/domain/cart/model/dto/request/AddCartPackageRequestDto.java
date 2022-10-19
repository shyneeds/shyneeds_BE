package com.example.shyneeds_be.domain.cart.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddCartPackageRequestDto {
    @ApiModelProperty(value = "상품 아이디", required = true, example = "1")
    private Long productId;

    @ApiModelProperty(value = "옵션 제목", required = true, example = "2022 시즌 매주 금요일 출발")
    private String optionTitle;

    @ApiModelProperty(value = "옵션 값", required = true, example = "11/04(금)출발 ~ 11/13(일)도착")
    private String optionValue;

    @ApiModelProperty(value = "가격", required = true, example = "4,390,000")
    private String price;

    @ApiModelProperty(value = "필수옵션 여부", required = true, example = "true")
    private boolean optionFlg;

    @ApiModelProperty(value = "수량", required = true, example = "1")
    private Integer quantity;
}
