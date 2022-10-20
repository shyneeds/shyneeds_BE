package com.example.shyneeds_be.domain.cart.controller;

import com.example.shyneeds_be.domain.cart.model.dto.request.AddCartRequestDto;
import com.example.shyneeds_be.domain.cart.model.dto.response.CartResponseDto;
import com.example.shyneeds_be.domain.cart.service.CartService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "[유저] 장바구니 추가")
    @PostMapping("/user/{id}")
    public ApiResponseDto addCart(@PathVariable("id") Long userId, @RequestBody AddCartRequestDto addCartRequest){
        return cartService.addCart(userId, addCartRequest);
    }

    @ApiOperation(value = "[유저] 장바구니 조회")
    @GetMapping("/user/{id}")
    public ApiResponseDto<CartResponseDto> getCartList(@PathVariable("id") Long userId){
        return cartService.getCartList(userId);
    }
}
