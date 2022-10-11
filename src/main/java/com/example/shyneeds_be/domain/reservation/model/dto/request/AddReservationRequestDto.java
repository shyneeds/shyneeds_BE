package com.example.shyneeds_be.domain.reservation.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddReservationRequestDto {

    @ApiModelProperty(value = "예약할 패키지 리스트", required = true, example = "")
    private List<ReservationPackageRequestDto> reservationPackages;

    @ApiModelProperty(value = "총 예약 금액", required = true, example = "3,000,000")
    private Long totalReservationAmount;

    @ApiModelProperty(value = "예약금 지불 수단", required = true, example = "무통장 입금")
    private String paymentMethod;

    @ApiModelProperty(value = "계좌번호", required = true, example = "신한은행 100-3333-4444(더샤이니)")
    private String paymentAccountNumber;

    @ApiModelProperty(value = "입금자명", required = true, example = "입금자")
    private String depositorName;

    @ApiModelProperty(value = "약관 동의 여부", required = true, example = "true")
    private Boolean serviceTerms;

}
