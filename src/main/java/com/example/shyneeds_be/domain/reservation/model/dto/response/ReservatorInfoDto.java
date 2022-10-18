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
public class ReservatorInfoDto {
// 예약자 이름, 이메일, 폰번호
    @ApiModelProperty(value = "예약자 이름", required = true, example = "홍길동")
    private String reservatorName;

    @ApiModelProperty(value = "예약자 전화번호", required = true, example = "010-1234-1234")
    private String reservatorPhoneNumber;

    @ApiModelProperty(value = "예약자 이메일", required = true, example = "email2@gmail.com")
    private String reservatorEmail;

}
