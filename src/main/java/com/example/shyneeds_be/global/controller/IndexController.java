package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class IndexController {

    @ApiOperation(value = "swagger test")
    @GetMapping("/index")
    public ApiResponseDto<IndexObject> index(){
        IndexObject apiResponse = IndexObject.builder()
                .title("2조 더샤이니")
                .subTitle("응답값 포맷 확인 API")
                .build();
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "인덱스 조회 성공", apiResponse);
    }

    @GetMapping("/admin/index")
    public ApiResponseDto<IndexObject> adminIndex(){
        IndexObject apiResponse = IndexObject.builder()
                .title("admin 인증")
                .subTitle("응답값 포맷 확인 API")
                .build();
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "인덱스 조회 성공", apiResponse);
    }
    @GetMapping("/user/index")
    public ApiResponseDto<IndexObject> userIndex(){
        IndexObject apiResponse = IndexObject.builder()
                .title("user 인증")
                .subTitle("응답값 포맷 확인 API")
                .build();
        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "인덱스 조회 성공", apiResponse);
    }
}
