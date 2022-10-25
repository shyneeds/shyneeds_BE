package com.example.shyneeds_be.domain.reservation.service;

import com.example.shyneeds_be.domain.reservation.model.dto.request.AddReservationRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.request.CancelReservationRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.request.ReservationPackageRequestDto;
import com.example.shyneeds_be.domain.reservation.model.dto.response.*;
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
import java.util.ArrayList;
import java.util.List;
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
                    .paymentAccountBank(addReservationRequest.getPaymentAccountBank())
                    .paymentAccountNumber(addReservationRequest.getPaymentAccountNumber())
                    .paymentAccountHolder(addReservationRequest.getPaymentAccountHolder())
                    .depositorName(addReservationRequest.getDepositorName())
                    .serviceTerms(addReservationRequest.getServiceTerms())
                    .totalReservationAmount(addReservationRequest.getTotalReservationAmount())
                    .reservationStatus(ReservationStatus.입금대기)
                    .reservationNumber(createReservationNumber())
                    .reservatorName(addReservationRequest.getReservatorName())
                    .reservatorPhoneNumber(addReservationRequest.getReservatorPhoneNumber())
                    .reservatorEmail(addReservationRequest.getReservatorEmail())
                    .user(user)
                    .build();

            reservationRepository.save(reservation);

            for (ReservationPackageRequestDto reservationPackageRequest : addReservationRequest.getReservationPackages()) {
                ReservationPackage reservationPackage = ReservationPackage.builder()
                        .optionTitle(reservationPackageRequest.getOptionTitle())
                        .optionValue(reservationPackageRequest.getOptionValue())
                        .price(reservationPackageRequest.getPrice())
                        .optionFlg(reservationPackageRequest.isOptionFlg())
                        .quantity(reservationPackageRequest.getQuantity())
                        .travelPackage(travelPackageRepository.findByPackageId(reservationPackageRequest.getTravelPackageId()))
                        .reservation(reservation)
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

    /*
        예약 상세 조회
    */
    public ApiResponseDto<ReservationDetailResponseDto> getReservationDetail(String reservationNumber) {
        try{
            System.out.println(reservationRepository.findByReservationNumber(reservationNumber));
            if(reservationRepository.findByReservationNumber(reservationNumber) != null){
                Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);
                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "예약 조회에 성공했습니다",
                        ReservationDetailResponseDto.builder()
                                .reservatedAt(reservation.getCreatedAt())
                                .reservationNumber(reservation.getReservationNumber())
                                .reservationPackageList(this.getReservationPackageList(reservation))
                                .reservatorInfo(this.getReservatorInfo(reservation))
                                .paymentInfo(this.getPaymentInfo(reservation))
                                .build());
            } else {
                return ApiResponseDto.of(ResponseStatusCode.NO_CONTENT.getValue(), "해당 예약번호의 예약이 없습니다.");
            }

        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "예약 조회에 실패했습니다. " + e.getMessage());
        }
    }

    private PaymentInfoDto getPaymentInfo(Reservation reservation) {
        return PaymentInfoDto.builder()
                .totalReservationAmount(reservation.getTotalReservationAmount())
                .paymentMethod(reservation.getPaymentMethod())
                .paymentAccountBank(reservation.getPaymentAccountBank())
                .paymentAccountNumber(reservation.getPaymentAccountNumber())
                .paymentAccountHolder(reservation.getPaymentAccountHolder())
                .depositorName(reservation.getDepositorName())
                .build();
    }

    private ReservatorInfoDto getReservatorInfo(Reservation reservation) {
        return ReservatorInfoDto.builder()
                .reservatorName(reservation.getReservatorName())
                .reservatorPhoneNumber(reservation.getReservatorPhoneNumber())
                .reservatorEmail(reservation.getReservatorEmail())
                .build();
    }

    private List<ReservationPackageDetailDto> getReservationPackageList(Reservation reservation) {
        List<ReservationPackage> reservationPackageList = reservationPackageRepository.findAllByReservationId(reservation.getId());
        List<ReservationPackageDetailDto> reservationPackageDetaiList = new ArrayList<>();

        for (ReservationPackage reservationPackage : reservationPackageList){

            String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/"+
                    reservationPackage.getTravelPackage().getTitle()+"/main/"+reservationPackage.getTravelPackage().getMainImage();

            reservationPackageDetaiList.add(
                    ReservationPackageDetailDto.builder()
                            .packageImage(imageDir)
                            .optionTitle(reservationPackage.getOptionTitle())
                            .optionValue(reservationPackage.getOptionValue())
                            .price(reservationPackage.getPrice())
                            .optionFlg(reservationPackage.isOptionFlg())
                            .quantity(reservationPackage.getQuantity())
                            .travelPackageId(reservationPackage.getTravelPackage().getId())
                            .build());
        }
        return reservationPackageDetaiList;
    }

    public ApiResponseDto cancelReservation(Long userId ,String reservationNumber, CancelReservationRequestDto cancelReservationRequest) {
        try {
            if (reservationRepository.findByReservationNumber(reservationNumber) != null) {
                if (reservationRepository.findAllByUserId(userId).contains(reservationRepository.findByReservationNumber(reservationNumber))) {
                    Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);
                    reservation.cancelReservation(cancelReservationRequest.getCancelReason(), cancelReservationRequest.getCancelReasonDetail());
                    reservationRepository.save(reservation);
                    return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "예약이 취소되었습니다.");
                } else {
                    return ApiResponseDto.of(ResponseStatusCode.FORBIDDEN.getValue(), "해당 유저의 예약번호가 아닙니다.");
                }
            } else {
                return ApiResponseDto.of(ResponseStatusCode.NO_CONTENT.getValue(), "해당 예약번호의 예약이 없습니다.");
            }

        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "예약취소에 실패했습니다 " + e.getMessage());
        }

    }

    public ApiResponseDto<ReservationCancelInfoResponseDto> getReservationCancelInfo(String reservationNumber) {
        try{
            if (reservationRepository.findByReservationNumber(reservationNumber) != null){
                Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber);
                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "취소 상세 조회를 성공했습니다",
                        ReservationCancelInfoResponseDto.builder()
                                .cancelReason(reservation.getCancelReason())
                                .cancelReasonDetail(reservation.getCancelReasonDetail())
                                .paymentMethod(reservation.getPaymentMethod())
                                .build());
            } else {
                return ApiResponseDto.of(ResponseStatusCode.NO_CONTENT.getValue(), "해당 예약번호의 예약이 없습니다.");
            }
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "취소 상세 조회에 실패했습니다 " + e.getMessage());
        }
    }
}

