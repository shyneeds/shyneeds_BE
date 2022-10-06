package com.example.shyneeds_be.global.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainRequestDto {

    @ApiModelProperty(value = "메인화면 카테고리별 상품 리스트를 위한 카테고리 리스트", required = true, example = "[지역상품별, 그룹상품별]")
    private List<String> categoryList;
}
