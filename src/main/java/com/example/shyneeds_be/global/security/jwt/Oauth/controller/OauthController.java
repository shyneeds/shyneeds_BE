package com.example.shyneeds_be.global.security.jwt.Oauth.controller;

import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.security.jwt.Oauth.dto.KakaoProfile;
import com.example.shyneeds_be.global.security.jwt.Oauth.dto.OauthToken;
import com.example.shyneeds_be.global.security.jwt.Oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/oauth/token")
    public ApiResponseDto<KakaoProfile> getLogin(@RequestHeader("Authorization") String code){

        // 넘어온 인가 코드를 통해 access_token 발급
//        OauthToken oauthToken = oauthService.getAccessToken(code);
        KakaoProfile kakaoProfile = oauthService.findKakaoProfile(code);

        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(),"카카오프로필" ,kakaoProfile);
    }
}
