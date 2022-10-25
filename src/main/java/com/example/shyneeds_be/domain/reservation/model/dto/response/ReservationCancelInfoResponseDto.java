package com.example.shyneeds_be.domain.reservation.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReservationCancelInfoResponseDto {

    private String cancelReason;

    private String cancelReasonDetail;

    private String paymentMethod;
}
