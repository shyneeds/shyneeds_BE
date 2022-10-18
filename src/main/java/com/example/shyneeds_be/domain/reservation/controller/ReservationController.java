package com.example.shyneeds_be.domain.reservation.controller;

import com.example.shyneeds_be.domain.reservation.model.dto.request.AddReservationRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.response.ReservationDetailResponseDto;
import com.example.shyneeds_be.domain.reservation.service.ReservationService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @ApiOperation(value = "[유저] 상품 예약")
    @PostMapping("/user/{id}")
    public ApiResponseDto addReservation(@PathVariable("id") Long userId, @RequestBody AddReservationRequestDto addReservationRequest){

        return reservationService.addReservation(userId, addReservationRequest);
    }

    @ApiOperation(value = "[유저] 예약정보 조회")
    @GetMapping("/user")
    public ApiResponseDto<ReservationDetailResponseDto> selectReservationDetail(@PathParam("reservation_number") String reservationNumber){
        System.out.println(reservationNumber);
        return reservationService.selectReservationDetail(reservationNumber);
    }
}
