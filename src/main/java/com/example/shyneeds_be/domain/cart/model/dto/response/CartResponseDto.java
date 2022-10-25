package com.example.shyneeds_be.domain.cart.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartResponseDto {

    private List<CartPackageResponseDto> cartList;

}
