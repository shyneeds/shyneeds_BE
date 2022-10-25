package com.example.shyneeds_be.global.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurationRequestDto {

    @ApiModelProperty(value = "연령그룹", required = false, example = "5070")
    private String age;

    @ApiModelProperty(value = "동행그룹", required = false, example = "여성그룹")
    private String accompany;

    @ApiModelProperty(value = "성향그룹", required = false, example = "진보적 스트라이커")
    private String tendency;

    @ApiModelProperty(value = "종교그룹", required = false, example = "기독교")
    private String religion;

    @ApiModelProperty(value = "지역그룹", required = false, example = "유럽")
    private String country;

    @ApiModelProperty(value = "테마그룹", required = false, example = "문화탐방")
    private String theme;
}
