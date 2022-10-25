package com.example.shyneeds_be.domain.reservation.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CancelReservationRequestDto {

    private String cancelReason;

    private String cancelReasonDetail;
}
