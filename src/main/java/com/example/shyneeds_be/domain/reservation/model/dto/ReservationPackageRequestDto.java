package com.example.shyneeds_be.domain.reservation.model.dto;

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
    private String selectRequiredOptionName;

    @ApiModelProperty(value = "필수옵션 값", required = true, example = "남성")
    private String selectRequiredOptionValues;

    @ApiModelProperty(value = "선택옵션 이름", required = true, example = "싱글룸")
    private String selectOptionalName;

    @ApiModelProperty(value = "선택옵션 값", required = true, example = "객실 1인 사용료")
    private String selectOptionalValues;

    @ApiModelProperty(value = "예약 가격", required = true, example = "3,000,000")
    private Long reservation_price;

    @ApiModelProperty(value = "수량", required = true, example = "1")
    private Integer quantity;

    @ApiModelProperty(value = "여행 패키지 ID", required = true, example = "1")
    private Long travelPackageId;
}
