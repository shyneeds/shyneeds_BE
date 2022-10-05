package com.example.shyneeds_be.domain.category.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    @ApiModelProperty(value = "고유번호", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "서브 카테고리 리스트", required = false, example = "")
    private List<SubCategoryResponseDto> subCategoryResponseDtoList;

    @ApiModelProperty(value = "노출여부", required = false, example = "")
    private boolean dispFlg;

    @ApiModelProperty(value = "데이터 생성일", required = false, example = "")
    private Timestamp createdAt;

    @ApiModelProperty(value = "데티서 수정일", required = false, example = "")
    private Timestamp updatedAt;

}
