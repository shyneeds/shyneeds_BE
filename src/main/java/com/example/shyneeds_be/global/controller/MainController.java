package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.global.model.dto.request.MainRequestDto;
import com.example.shyneeds_be.global.model.dto.response.MainResponseDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 메인화면 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/api/main")
@RestController
public class MainController {

    private final MainService mainService;

    @PostMapping("")
    public ApiResponseDto<MainResponseDto> main(@RequestBody MainRequestDto mainRequestDto){
        return mainService.getMain(mainRequestDto);
    }


}
