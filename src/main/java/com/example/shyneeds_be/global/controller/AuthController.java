package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.auth.dto.*;
import com.example.shyneeds_be.domain.user.service.UserService;
import com.example.shyneeds_be.global.auth.service.OauthService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final OauthService oauthService;
    @PostMapping("/login")
    public ApiResponseDto<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        AuthResponseDto authResponse = oauthService.login(loginRequestDto);
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(),"로그인 성공", authResponse);
    }

    @PostMapping("/signup")
    public ApiResponseDto singup(@RequestBody SignupRequestDto signupRequestDto){
        oauthService.signup(signupRequestDto);
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "회원가입 성공");
    }
    @PostMapping("/login/kakao")
    public ApiResponseDto<AuthResponseDto> kakaoLogin(@RequestHeader("Authorization") AuthRequestDto authAccessToken){
        AuthResponseDto authResponse = oauthService.kakaoLogin(authAccessToken);
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "로그인 성공", authResponse);
    }



}
