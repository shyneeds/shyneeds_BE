package com.example.shyneeds_be.domain.reservation.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReservationPackageDetailDto {
    // 상품id, 사진, 옵션 타이틀, 옵션 값, 가격, 수량, 예약상태

    @ApiModelProperty(value = "상품 이미지", required = true, example = "")
    private String packageImage;

    @ApiModelProperty(value = "옵션 이름", required = true, example = "성별")
    private String optionTitle;

    @ApiModelProperty(value = "옵션 값", required = true, example = "남성")
    private String optionValue;

    @ApiModelProperty(value = "예약 가격", required = true, example = "3,000,000")
    private String price;

    @ApiModelProperty(value = "필수 옵션 여부", required = true, example = "true")
    private boolean optionFlg;

    @ApiModelProperty(value = "수량", required = true, example = "1")
    private Integer quantity;

    @ApiModelProperty(value = "여행 패키지 ID", required = true, example = "1")
    private Long travelPackageId;
}
