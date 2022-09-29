package com.example.shyneeds_be.global.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 테스트를 위한 객체.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndexObject {

    @ApiModelProperty(value = "타이틀")
    private String title;

    @ApiModelProperty(value = "서브 타이틀")
    private String subTitle;
}
