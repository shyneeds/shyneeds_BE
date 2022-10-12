package com.example.shyneeds_be.domain.user.controller;

import com.example.shyneeds_be.domain.user.model.dto.request.UpdateUserRequestDto;
import com.example.shyneeds_be.domain.user.service.UserService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "유저 정보 수정 기능")
    @PatchMapping("/{id}")
    public ApiResponseDto updateUserInfo(@PathVariable("id") Long userId, @RequestBody UpdateUserRequestDto updateUserRequest){
        return userService.updateUser(userId, updateUserRequest);
    }

    @ApiOperation(value = "회원 탈퇴 기능")
    @DeleteMapping("/{id}")
    public ApiResponseDto deleteUser(@PathVariable("id") Long userId){
        return userService.deleteUser(userId);
    }

}
