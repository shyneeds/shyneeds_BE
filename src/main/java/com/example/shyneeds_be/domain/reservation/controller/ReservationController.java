package com.example.shyneeds_be.domain.reservation.controller;

import com.example.shyneeds_be.domain.reservation.model.dto.request.AddReservationRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.request.CancelReservationRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.response.ReservationCancelInfoResponseDto;
import com.example.shyneeds_be.domain.reservation.model.dto.response.ReservationDetailResponseDto;
import com.example.shyneeds_be.domain.reservation.service.ReservationService;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @ApiOperation(value = "[유저] 상품 예약")
    @PostMapping("/user")
    public ApiResponseDto addReservation(@AuthenticationPrincipal User user, @RequestBody AddReservationRequestDto addReservationRequest){
        return reservationService.addReservation(user, addReservationRequest);
    }

    @ApiOperation(value = "예약정보 조회")
    @GetMapping("")
    public ApiResponseDto<ReservationDetailResponseDto> getReservationDetail(@RequestParam(name = "reservation_number") String reservationNumber){
        return reservationService.getReservationDetail(reservationNumber);
    }

    @ApiOperation(value = "예약 취소")
    @DeleteMapping("/user")
    public ApiResponseDto cancelReservation(@AuthenticationPrincipal User user, @RequestParam(name = "reservation_number") String reservationNumber, @RequestBody CancelReservationRequestDto cancelReservationRequest){
        return reservationService.cancelReservation(user, reservationNumber, cancelReservationRequest);
    }

    @ApiOperation(value = "취소 상세")
    @GetMapping("/cancel")
    public ApiResponseDto<ReservationCancelInfoResponseDto> getReservationCancelInfo(@RequestParam(name = "reservation_number") String reservationNumber){
        return reservationService.getReservationCancelInfo(reservationNumber);
    }
}
