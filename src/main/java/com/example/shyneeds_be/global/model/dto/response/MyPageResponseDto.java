package com.example.shyneeds_be.global.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MyPageResponseDto {

    private MyPageUserResponseDto userInfo;

    private List<MyPageReservationResponseDto> reservationList;
}
