package com.example.shyneeds_be.domain.user.controller;

import com.example.shyneeds_be.domain.user.model.dto.request.UpdateUserRequestDto;
import com.example.shyneeds_be.domain.user.service.UserService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ApiResponseDto updateUserInfo(@PathVariable("id") Long id, @RequestBody UpdateUserRequestDto updateUserRequest){
        return userService.updateUser(id, updateUserRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }

}
