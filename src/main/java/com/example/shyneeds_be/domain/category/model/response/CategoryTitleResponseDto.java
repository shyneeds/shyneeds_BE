package com.example.shyneeds_be.domain.category.model.response;

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
public class CategoryTitleResponseDto {

    @ApiModelProperty(value = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "서브 카테고리 타이틀", required = false, example = "")
    private String subCategoryTitle;

}
