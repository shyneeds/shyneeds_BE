package com.example.shyneeds_be.domain.travel_package.controller;

import com.example.shyneeds_be.domain.travel_package.model.dto.response.GroupPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.model.dto.response.TravelPackageResponseDto;
import com.example.shyneeds_be.domain.travel_package.service.GroupPackageService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupPackageController {

    private final GroupPackageService groupPackageService;

    @ApiOperation(value = "그룹 여행")
    @GetMapping("")
    public ApiResponseDto<Map<String,List<GroupPackageResponseDto>>> getGroupPackage(@PathParam("name") String name){
        return groupPackageService.getGroupPackage(name);
    }

    @ApiOperation(value = "서브 카테고리 별 그룹 여행")
    @GetMapping("/sub")
    public ApiResponseDto<List<GroupPackageResponseDto>> getGroupPackageListBySubCategory(@PathParam("name") String name, @PathParam("sortFlg") String sortFlg){
        return groupPackageService.getGroupPackageListBySubCategory(name, sortFlg);
    }

}
