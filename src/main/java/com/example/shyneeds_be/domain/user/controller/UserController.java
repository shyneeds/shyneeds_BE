package com.example.shyneeds_be.domain.user.controller;

import com.example.shyneeds_be.domain.user.model.dto.request.UpdateUserRequestDto;
import com.example.shyneeds_be.domain.user.service.UserService;
import com.example.shyneeds_be.global.auth.jwt.Auth;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "유저 정보 수정 기능")
    @Auth
    @PatchMapping(value=  "",  consumes="multipart/form-data")
    public ApiResponseDto updateUserInfo(HttpServletRequest req,
                                         @RequestPart(value = "userInfo") UpdateUserRequestDto updateUserRequest,
                                         @RequestPart("profileImage") MultipartFile profileImage){
        return userService.updateUser((Long)req.getAttribute("userId"), updateUserRequest, profileImage);
    }

    @ApiOperation(value = "회원 탈퇴 기능")
    @Auth
    @DeleteMapping("")
    public ApiResponseDto deleteUser(HttpServletRequest req){
        return userService.deleteUser((Long)req.getAttribute("userId"));
    }
}
