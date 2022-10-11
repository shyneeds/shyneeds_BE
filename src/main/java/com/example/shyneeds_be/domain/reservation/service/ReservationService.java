package com.example.shyneeds_be.domain.reservation.service;

import com.example.shyneeds_be.domain.reservation.model.dto.request.AddReservationRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.request.ReservationPackageRequestDto;
import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import com.example.shyneeds_be.domain.reservation.model.enums.ReservationStatus;
import com.example.shyneeds_be.domain.reservation.repository.ReservationRepository;
import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import com.example.shyneeds_be.domain.reservation_package.repository.ReservationPackageRepository;
import com.example.shyneeds_be.domain.travel_package.repository.TravelPackageRepository;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReservationPackageRepository reservationPackageRepository;
    private final TravelPackageRepository travelPackageRepository;

//  예약하기
    public ApiResponseDto addReservation(Long userId, AddReservationRequestDto addReservationRequest){
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("없는 사용자입니다."));
            Reservation reservation = Reservation.builder()
                    .paymentMethod(addReservationRequest.getPaymentMethod())
                    .paymentAccountNumber(addReservationRequest.getPaymentAccountNumber())
                    .depositorName(addReservationRequest.getDepositorName())
                    .serviceTerms(addReservationRequest.getServiceTerms())
                    .totalReservationAmount(addReservationRequest.getTotalReservationAmount())
                    .reservationStatus(ReservationStatus.입금대기)
                    .reservationNumber(createReservationNumber())
                    .user(user)
                    .build();
            reservationRepository.save(reservation);

            for (ReservationPackageRequestDto reservationPackageRequest : addReservationRequest.getReservationPackages()) {
                ReservationPackage reservationPackage = ReservationPackage.builder()
                        .selectRequiredOptionName(reservationPackageRequest.getSelectRequiredOptionName())
                        .selectRequiredOptionValues(reservationPackageRequest.getSelectRequiredOptionValues())
                        .selectOptionalName(reservationPackageRequest.getSelectOptionalName())
                        .selectOptionalValues(reservationPackageRequest.getSelectOptionalValues())
                        .quantity(reservationPackageRequest.getQuantity())
                        .reservation_price(reservationPackageRequest.getReservation_price())
                        .travelPackage(travelPackageRepository.findByPackageId(reservationPackageRequest.getTravelPackageId()))
                        .reservation(reservation)
                        .user(user)
                        .build();
                reservationPackageRepository.save(reservationPackage);
            }
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "예약에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "예약에 실패했습니다." + e.getMessage());
        }
    }

//  예약번호 생성 (날짜 + 네자리 난수)
    public String createReservationNumber(){
        String pattern = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        Random random = new Random();
        int createNum = 0;
        String ranNum = "";
        int letter = 4;
        String resultNum = "";

        for (int i=0; i<letter; i++){
            createNum = random.nextInt(9);
            ranNum = Integer.toString(createNum);
            resultNum += ranNum;
        }

        return date+resultNum;
    }
}

