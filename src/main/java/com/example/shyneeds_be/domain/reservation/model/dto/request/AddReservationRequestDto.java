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
    private String totalReservationAmount;

    @ApiModelProperty(value = "예약금 지불 수단", required = true, example = "무통장 입금")
    private String paymentMethod;

    @ApiModelProperty(value = "입금 은행", required = true, example = "신한은행")
    private String paymentAccountBank;

    @ApiModelProperty(value = "계좌번호", required = true, example = "100-3333-4444")
    private String paymentAccountNumber;

    @ApiModelProperty(value = "예금주", required = true, example = "더 샤이니")
    private String paymentAccountHolder; // 예금주

    @ApiModelProperty(value = "입금자명", required = true, example = "입금자")
    private String depositorName;

    @ApiModelProperty(value = "약관 동의 여부", required = true, example = "true")
    private Boolean serviceTerms;

    @ApiModelProperty(value = "예약자 이름", required = true, example = "홍길동")
    private String reservatorName;

    @ApiModelProperty(value = "예약자 전화번호", required = true, example = "010-1234-1234")
    private String reservatorPhoneNumber;

    @ApiModelProperty(value = "예약자 이메일", required = true, example = "email2@gmail.com")
    private String reservatorEmail;

}
