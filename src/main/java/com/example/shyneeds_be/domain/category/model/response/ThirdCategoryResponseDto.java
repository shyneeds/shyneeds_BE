package com.example.shyneeds_be.domain.category.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThirdCategoryResponseDto {

    @ApiModelProperty(value = "고유번호", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "서브 카테고리 고유번호", required = false, example = "")
    private Long subCategoryId;

    @ApiModelProperty(value = "노출 여부", required = false, example = "")
    private boolean dispFlg;

    @ApiModelProperty(value = "데이터 생성일", required = false, example = "")
    private Timestamp createdAt;

    @ApiModelProperty(value = "데이터 수정일", required = false, example = "")
    private Timestamp updatedAt;

}
