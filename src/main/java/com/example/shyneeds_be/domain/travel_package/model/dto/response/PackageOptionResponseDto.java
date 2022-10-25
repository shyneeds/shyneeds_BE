package com.example.shyneeds_be.domain.travel_package.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageOptionResponseDto {

    @ApiModelProperty(name = "고유번호", required = false, example = "")
    private Long id;

    @ApiModelProperty(name = "타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(name = "옵션값", required = false, example = "")
    private String optionValue;

    @ApiModelProperty(name = "가격", required = false, example = "")
    private String price;

    @ApiModelProperty(name = "필수 옵션 여부", required = false, example = "")
    private boolean optionFlg;

    @ApiModelProperty(name = "데이터 생성일", required = false, example = "")
    private Timestamp createdAt;

    @ApiModelProperty(name = "데이터 수정일", required = false, example = "")
    private Timestamp updatedAt;


}
