package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.auth.dto.KakaoProfile;
import com.example.shyneeds_be.global.auth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.auth.dto.OauthToken;
import com.example.shyneeds_be.global.auth.dto.TokenInfoDto;
import com.example.shyneeds_be.global.auth.service.OauthService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/login/kakao")
    public ApiResponseDto<KakaoProfile> getLogin(@RequestHeader("Authorization") String code){

        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = oauthService.getAccessToken(code);
        KakaoProfile kakaoProfile = oauthService.findKakaoProfile(oauthToken);


        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(),"카카오프로필" ,kakaoProfile);
    }



}
