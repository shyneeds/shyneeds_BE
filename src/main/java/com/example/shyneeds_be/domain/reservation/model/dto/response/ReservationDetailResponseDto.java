package com.example.shyneeds_be.domain.reservation.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReservationDetailResponseDto {
    // 예약일자, 주문번호, 상품정보, id, 사진, 옵션 타이틀, 옵션 값, 가격, 수량, 예약상태,, 총 예약 금액, 결제방식, 이체은행, 게좌번호, 예금주, 구매자정보(예약자, 연락처, 이메일)
    // 예약정보, 예약패키지 정보, 예약자 정보, 계좌정보
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp reservatedAt;

    private String reservationNumber;

    private List<ReservationPackageDetailDto> reservationPackageList;

    private ReservatorInfoDto reservatorInfo;

    private PaymentInfoDto paymentInfo;


}
