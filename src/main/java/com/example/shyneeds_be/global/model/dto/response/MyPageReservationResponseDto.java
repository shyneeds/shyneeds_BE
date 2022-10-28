package com.example.shyneeds_be.global.model.dto.response;

import com.example.shyneeds_be.domain.reservation.model.enums.ReservationStatus;
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
public class MyPageReservationResponseDto {

    private Long reservationId;

    private String reservationNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Timestamp reservatedAt;

    private ReservationStatus reservationStatus;

    private String totalReservationAmount;

    private List<MyPageReservationPackageResponseDto> reservationPackage;


}
