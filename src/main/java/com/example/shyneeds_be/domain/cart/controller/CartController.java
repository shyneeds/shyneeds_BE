package com.example.shyneeds_be.domain.cart.controller;

import com.example.shyneeds_be.domain.cart.model.dto.request.AddCartRequestDto;
import com.example.shyneeds_be.domain.cart.model.dto.response.CartResponseDto;
import com.example.shyneeds_be.domain.cart.service.CartService;
import com.example.shyneeds_be.global.auth.jwt.Auth;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "[유저] 장바구니 추가")
    @Auth
    @PostMapping("/user")
    public ApiResponseDto addCart(HttpServletRequest req, @RequestBody AddCartRequestDto addCartRequest){
        return cartService.addCart((Long)req.getAttribute("userId"), addCartRequest);
    }

    @ApiOperation(value = "[유저] 장바구니 조회")
    @Auth
    @GetMapping("/user")
    public ApiResponseDto<CartResponseDto> getCartList(HttpServletRequest req){
        return cartService.getCartList((Long)req.getAttribute("userId"));
    }
}
