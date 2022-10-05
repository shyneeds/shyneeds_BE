package com.example.shyneeds_be.domain.user.controller;

import com.example.shyneeds_be.domain.user.model.dto.SignupRequestDto;
import com.example.shyneeds_be.domain.user.service.UserService;
import com.example.shyneeds_be.global.auth.dto.AccessTokenResponseDto;
import com.example.shyneeds_be.global.auth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ApiResponseDto<AccessTokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        AccessTokenResponseDto accessToken = userService.login(loginRequestDto);
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(),"로그인 성공", accessToken);
    }

    @PostMapping("/signup")
    public ApiResponseDto singup(@RequestBody SignupRequestDto signupRequestDto){
        userService.signup(signupRequestDto);
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "회원가입 성공");
    }
}
