package com.example.shyneeds_be.domain.travel_package.controller;

import com.example.shyneeds_be.domain.travel_package.model.dto.request.TravelPackageRegisterRequestDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.TravelPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.service.TravelPackageService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/package")
public class TravelPackageController {

    private final TravelPackageService travelPackageService;





    /* ---------------------------- ADMIN ---------------------------- */
    @ApiOperation(value = "[어드민] 상품 등록")
    @PostMapping(value = "/admin/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto registerPackage(
            @RequestParam(value = "registerInfo") TravelPackageRegisterRequestDto travelPackageRegisterRequestDto,
            @RequestPart(value = "main")MultipartFile mainImage,
            @RequestPart(value = "description", required = false)List<MultipartFile> descriptionImages
            ){

        return travelPackageService.register(travelPackageRegisterRequestDto, mainImage, descriptionImages);
    }


    @ApiOperation(value = "[어드민] 상품 리스트 조회")
    @GetMapping("/admin")
    public ApiResponseDto<List<TravelPackageResponseDto>> getPackageList(){
        return travelPackageService.getAdminPackageList();
    }

    @ApiOperation(value = "[어드민] 상품 조회")
    @GetMapping("/admin/{id}")
    public ApiResponseDto<TravelPackageResponseDto> getPackage(@PathVariable("id") Long id){
        return travelPackageService.getAdminPackage(id);
    }


    @ApiOperation(value = "[어드민] 상품 삭제")
    @PostMapping("/admin/delete/{id}")
    public ApiResponseDto deletePackage(@PathVariable("id") Long id){
        return travelPackageService.deletedPackage(id);
    }
}
