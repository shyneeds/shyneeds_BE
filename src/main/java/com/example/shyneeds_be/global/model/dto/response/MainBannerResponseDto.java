package com.example.shyneeds_be.global.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainBannerResponseDto {
    @ApiModelProperty(value = "고유번호", required = false, example = "")
    private Long id;

    @ApiModelProperty(value = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "이미지 url", required = false, example = "")
    private String imageUrl; // Main

    @ApiModelProperty(value = "검색키워드", required = false, example = "")
    private String keyword;
}
