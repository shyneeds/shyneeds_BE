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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "유저 정보 수정 기능")
    @Auth
    @PatchMapping(value=  "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponseDto updateUserInfo(@PathVariable("id") Long userId,
                                         @RequestPart("updateUserRequest") UpdateUserRequestDto updateUserRequest,
                                         @RequestPart("profileImage") MultipartFile profileImage){
        return userService.updateUser(userId, updateUserRequest, profileImage);
    }

    @ApiOperation(value = "회원 탈퇴 기능")
    @DeleteMapping("/{id}")
    public ApiResponseDto deleteUser(@PathVariable("id") Long userId){
        return userService.deleteUser(userId);
    }

}
