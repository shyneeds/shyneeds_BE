package com.example.shyneeds_be.domain.member.controller;

import com.example.shyneeds_be.domain.member.model.dto.request.*;
import com.example.shyneeds_be.domain.member.service.MemberService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "일반 로그인")
    @PostMapping("/login")
    public ApiResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return memberService.login(loginRequestDto);
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ApiResponseDto singup(@RequestBody SignupRequestDto signupRequestDto){
        return memberService.signup(signupRequestDto);
    }

    @ApiOperation(value = "카카오 로그인")
    @PostMapping("/login/kakao")
    public ApiResponseDto kakaoLogin(@RequestHeader("Authorization") AuthRequestDto authAccessToken){
        return memberService.kakaoLogin(authAccessToken);
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/refresh")
    public ApiResponseDto validateRefreshToken(@RequestBody ValidateRefreshRequestDto validateRefreshRequest){
        return memberService.validateRefreshToken(validateRefreshRequest);
    }

    @ApiOperation(value = "멤버 정보 수정 기능")
    @PatchMapping(value=  "/member",  consumes="multipart/form-data")
    public ApiResponseDto updateUserInfo(@AuthenticationPrincipal User user,
                                         @RequestPart(value = "userInfo") UpdateMemberRequestDto updateUserRequest,
                                         @RequestPart("profileImage") MultipartFile profileImage){
        return memberService.updateUser(user, updateUserRequest, profileImage);
    }

    @ApiOperation(value = "회원 탈퇴 기능")
    @DeleteMapping("")
    public ApiResponseDto deleteUser(@AuthenticationPrincipal User user){
        return memberService.deleteUser(user);
    }
}
