package com.example.shyneeds_be.domain.travel_package.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageOptionRequestDto {

    @ApiModelProperty(name = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(name = "옵션값", required = false, example = "")
    private String optionValue;

    @ApiModelProperty(name = "가격", required = false, example = "")
    private String price;

    @ApiModelProperty(name = "필수 옵션 여부", required = false, example = "")
    private boolean optionFlg;
}
