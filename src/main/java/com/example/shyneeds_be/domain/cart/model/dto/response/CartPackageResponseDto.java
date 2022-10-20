package com.example.shyneeds_be.domain.cart.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartPackageResponseDto {
    // 상품리스트(상품 아이디, 상품 이미지, 제목, 옵션명, 옵션값, 필수여부, 갯수, 가격)
    private Long productId;

    private String productImage;

    private String title;

    private String optionTitle;

    private String optionValue;

    private boolean optionFlg;

    private Integer quantity;

    private String price;
}
