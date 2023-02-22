package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.security.oauth.dto.AuthRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.AuthResponseDto;
import com.example.shyneeds_be.global.security.oauth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.SignupRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.request.ValidateRefreshRequestDto;
import com.example.shyneeds_be.global.security.oauth.service.OauthService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final OauthService oauthService;
    @PostMapping("/login")
    public ApiResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return oauthService.login(loginRequestDto);
    }

    @PostMapping("/signup")
    public ApiResponseDto singup(@RequestBody SignupRequestDto signupRequestDto){
        return oauthService.signup(signupRequestDto);
    }
    @PostMapping("/login/kakao")
    public ApiResponseDto kakaoLogin(@RequestHeader("Authorization") AuthRequestDto authAccessToken){
        return oauthService.kakaoLogin(authAccessToken);
    }

    @PostMapping("/refresh")
    public ApiResponseDto validateRefreshToken(@RequestBody ValidateRefreshRequestDto validateRefreshRequest){
        return oauthService.validateRefreshToken(validateRefreshRequest);
    }
}
