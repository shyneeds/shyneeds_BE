package com.example.shyneeds_be.global.model.dto.response;

import com.example.shyneeds_be.domain.reservation.model.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MyPageReservationResponseDto {

    private String reservationNumber;

    private Timestamp reservatedAt;

    private ReservationStatus reservationStatus;

    private String totalReservationAmount;

    private List<MyPageReservationPackageResponseDto> reservationPackage;


}
