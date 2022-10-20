package com.example.shyneeds_be.domain.cart.service;

import com.example.shyneeds_be.domain.cart.model.dto.request.AddCartPackageRequestDto;
import com.example.shyneeds_be.domain.cart.model.dto.request.AddCartRequestDto;
import com.example.shyneeds_be.domain.cart.model.dto.response.CartPackageResponseDto;
import com.example.shyneeds_be.domain.cart.model.dto.response.CartResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
@Transactional
    public ApiResponseDto<CartResponseDto> getCartList(Long userId) {
        try{
            User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("없는 유저입니다."));
            List<Cart> cartList = cartRepository.findAllByUserId(user.getId());
            List<CartPackageResponseDto> cartPackageList = new ArrayList<>();

            for(Cart cart : cartList){
                cartPackageList.add(
                        CartPackageResponseDto.builder()
                                .productId(cart.getTravelPackage().getId())
                                .productImage(cart.getTravelPackage().getMainImage())
                                .title(cart.getTravelPackage().getTitle())
                                .optionTitle(cart.getOptionTitle())
                                .optionValue(cart.getOptionValue())
                                .optionFlg(cart.isOptionFlg())
                                .quantity(cart.getQuantity())
                                .price(cart.getPrice())
                                .build()
                );
            }

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "장바구니 조회를 성공했습니다",
                    CartResponseDto.builder()
                            .cartList(cartPackageList)
                            .build());

        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "장바구니 조회에 실패했습니다. " + e.getMessage());
        }
    }
}
