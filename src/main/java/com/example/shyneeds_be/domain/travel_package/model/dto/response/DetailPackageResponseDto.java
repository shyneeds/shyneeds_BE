package com.example.shyneeds_be.domain.travel_package.model.dto.response;

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
public class DetailPackageResponseDto {

    @ApiModelProperty(value = "상품 정보", required = true, example = "")
    TravelPackageResponseDto travelPackageResponseDto;

    @ApiModelProperty(value = "연관 상품 리스트", required = false, example = "")
    List<TravelPackageResponseDto> relatedPackageList;
}
