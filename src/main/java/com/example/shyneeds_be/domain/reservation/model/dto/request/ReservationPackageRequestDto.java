package com.example.shyneeds_be.domain.reservation.model.dto.request;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationPackageRequestDto {

    @ApiModelProperty(value = "필수옵션 이름", required = true, example = "성별")
    private String title;

    @ApiModelProperty(value = "필수옵션 값", required = true, example = "남성")
    private String optionValue;

    @ApiModelProperty(value = "예약 가격", required = true, example = "3,000,000")
    private String price;

    @ApiModelProperty(value = "필수 옵션 여부", required = true, example = "1")
    private boolean optionFlg;

    @ApiModelProperty(value = "수량", required = true, example = "1")
    private Integer quantity;

    @ApiModelProperty(value = "여행 패키지 ID", required = true, example = "1")
    private Long travelPackageId;
}
