package com.example.shyneeds_be.domain.community.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitPackageResponseDto {

    @ApiModelProperty(value = "상품 고유번호", required = true, example = "")
    private Long id;

    @ApiModelProperty(value = "타이틀", required = true, example = "")
    private String title;

    @ApiModelProperty(value = "메인 이미지", required = true, example = "")
    private String mainImage;
}
