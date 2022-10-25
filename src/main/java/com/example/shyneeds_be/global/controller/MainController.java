package com.example.shyneeds_be.global.controller;

import com.example.shyneeds_be.domain.category.model.response.CategoryTitleResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.TravelPackageResponseDto;
import com.example.shyneeds_be.global.model.dto.request.CurationRequestDto;
import com.example.shyneeds_be.global.model.dto.request.MainRequestDto;
import com.example.shyneeds_be.global.model.dto.response.MainResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MainTravelPackageResponseDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.service.MainService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 메인화면 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/api/main")
@RestController
public class MainController {

    private final MainService mainService;

    @ApiOperation(value = "메인 화면 API. 메인 배너, 카테고리 별 상품 리스트 출력")
    @PostMapping("")
    public ApiResponseDto<MainResponseDto> main(@RequestBody MainRequestDto mainRequestDto) {
        return mainService.getMain(mainRequestDto);
    }

    @ApiOperation(value = "큐레이션 API")
    @PostMapping("/curation")
    public ApiResponseDto<List<MainTravelPackageResponseDto>> getResultCuration(@RequestBody CurationRequestDto curationRequestDto) {
        return mainService.getResultCuration(curationRequestDto);
    }

    @ApiOperation(value = "큐레이션 카테고리 리스트")
    @GetMapping("/category")
    public ApiResponseDto<Map<String, List<CategoryTitleResponseDto>>> getCategoryTitleList(){
        return mainService.getCategoryTitleList();
    }
}
