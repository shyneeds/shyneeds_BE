package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.model.dto.response.MyPageResponseDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my/user")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("")
    public ApiResponseDto<MyPageResponseDto> getMyPageMain(HttpServletRequest req){
        return myPageService.getMyPageMain((Long) req.getAttribute("userId"));
    }

}
