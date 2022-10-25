package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.auth.dto.*;
import com.example.shyneeds_be.global.auth.dto.request.ValidateRefreshRequestDto;
import com.example.shyneeds_be.global.auth.dto.response.RecreatedAccessTokenResponseDto;
import com.example.shyneeds_be.global.auth.jwt.Auth;
import com.example.shyneeds_be.global.auth.service.OauthService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final OauthService oauthService;
    @PostMapping("/login")
    public ApiResponseDto<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return oauthService.login(loginRequestDto);
    }

    @PostMapping("/signup")
    public ApiResponseDto singup(@RequestBody SignupRequestDto signupRequestDto){
        return oauthService.signup(signupRequestDto);
    }
    @PostMapping("/login/kakao")
    public ApiResponseDto<AuthResponseDto> kakaoLogin(@RequestHeader("Authorization") AuthRequestDto authAccessToken){
        return oauthService.kakaoLogin(authAccessToken);
    }

    @Auth
    @PostMapping("/refresh")
    public ApiResponseDto<RecreatedAccessTokenResponseDto> validateRefreshToken(HttpServletRequest req, @RequestHeader("REFRESH_TOKEN") ValidateRefreshRequestDto validateRefreshRequest){
        return oauthService.validateRefreshToken((Long) req.getAttribute("userId"), validateRefreshRequest);
    }
}
