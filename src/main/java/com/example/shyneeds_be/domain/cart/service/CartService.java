package com.example.shyneeds_be.domain.cart.service;

import com.example.shyneeds_be.domain.cart.model.dto.request.AddCartPackageRequestDto;
import com.example.shyneeds_be.domain.cart.model.dto.request.AddCartRequestDto;
import com.example.shyneeds_be.domain.cart.model.entity.Cart;
import com.example.shyneeds_be.domain.cart.repository.CartRepository;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.travel_package.repository.TravelPackageRepository;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService
{
    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final TravelPackageRepository travelPackageRepository;

    /*
        장바구니 추가
    */
    public ApiResponseDto addCart(Long userId, AddCartRequestDto addCartRequest) {
        try{
            User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("없는 유저입니다."));

            for (AddCartPackageRequestDto addCartPackageRequest : addCartRequest.getAddCartList()) {
                TravelPackage travelPackage = travelPackageRepository.findByPackageId(addCartPackageRequest.getProductId());

                cartRepository.save(
                        Cart.builder()
                                .optionFlg(addCartPackageRequest.isOptionFlg())
                                .optionTitle(addCartPackageRequest.getOptionTitle())
                                .optionValue(addCartPackageRequest.getOptionValue())
                                .price(addCartPackageRequest.getPrice())
                                .quantity(addCartPackageRequest.getQuantity())
                                .travelPackage(travelPackage)
                                .user(user)
                                .build()
                );
            }
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "상품을 장바구니에 추가했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "장바구니 추가에 실패했습니다. " +e.getMessage());
        }
    }
}
