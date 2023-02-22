package com.example.shyneeds_be.global.service;

import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import com.example.shyneeds_be.domain.reservation.repository.ReservationRepository;
import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import com.example.shyneeds_be.domain.reservation_package.repository.ReservationPackageRepository;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import com.example.shyneeds_be.domain.member.repository.MemberRepository;
import com.example.shyneeds_be.global.model.dto.response.MyPageReservationPackageResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MyPageReservationResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MyPageResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MyPageUserResponseDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;

    private final ReservationRepository reservationRepository;

    private final ReservationPackageRepository reservationPackageRepository;
    @Transactional
    public ApiResponseDto<MyPageResponseDto> getMyPageMain(Long id) {
        try{
            Member member = memberRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("없는 유저입니다."));

            MyPageResponseDto myPageResponse = myPageMainResponse(member);
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "마이페이지 데이터 불러오기 성공 ", myPageResponse);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "마이페이지 데이터 불러오기 실패 " + e.getMessage());
        }
    }

    @Transactional
    public MyPageResponseDto myPageMainResponse(Member member) {

        String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/user/"+
                member.getId().toString();
        return MyPageResponseDto.builder()
                .userInfo(
                        MyPageUserResponseDto.builder()
                                .profileImage(imageDir+"/"+ member.getProfileImage())
                                .email(member.getEmail())
                                .name(member.getName())
                                .phoneNumber(member.getPhoneNumber())
                                .birthday(member.getBirthday())
                                .gender(member.getGender())
                                .totalPaymentAmount(member.getTotalPaymentAmount())
                        .build())
                .reservationList(myPageReservationResponse(member.getId()))
                .build();

    }

    // 예약리스트 전부 받아와 -> 예약번호 별 상품 목록 리스트 -> 상품 목록
    // 마이 예약리스트(예약번호, 예약일시, 예약상품리스트, 예약 상태)
    // 마이 예약상품리스트(상품아이디, 상품명, 옵션명, 옵션 가치, 가격)
    @Transactional
    public List<MyPageReservationResponseDto> myPageReservationResponse(Long userId){

        if (reservationRepository.findAllByMemberId(userId) != null) {
            List<Reservation> reservationList = reservationRepository.findAllByMemberId(userId);
            List<MyPageReservationResponseDto> myPageReservationResponseList = new ArrayList<>();

            for (Reservation reservation : reservationList) {
                myPageReservationResponseList.add(
                        MyPageReservationResponseDto.builder()
                                .reservationId(reservation.getId())
                                .reservationNumber(reservation.getReservationNumber())
                                .reservatedAt(reservation.getCreatedAt())
                                .reservationStatus(reservation.getReservationStatus())
                                .totalReservationAmount(reservation.getTotalReservationAmount())
                                .reservationPackage(myPageReservationPackageResponse(reservation.getId()))
                                .build()
            );
            }

            return myPageReservationResponseList;
        } else{

            return null;
        }
    }

    @Transactional
    public List<MyPageReservationPackageResponseDto> myPageReservationPackageResponse(Long reservationId){

        List<ReservationPackage> reservationPackageList = reservationPackageRepository.findAllByReservationId(reservationId);
        List<MyPageReservationPackageResponseDto> myPageReservationPackageResponseList = new ArrayList<>();

        for(ReservationPackage reservationPackage : reservationPackageList){

            String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/package/"+
                    reservationPackage.getTravelPackage().getTitle()+"/main/"+reservationPackage.getTravelPackage().getMainImage();

            myPageReservationPackageResponseList.add(
                    MyPageReservationPackageResponseDto.builder()
                            .packageId(reservationPackage.getTravelPackage().getId())
                            .imageUrl(imageDir)
                            .title(reservationPackage.getTravelPackage().getTitle())
                            .optionTitle(reservationPackage.getOptionTitle())
                            .optionValue(reservationPackage.getOptionValue())
                            .quantity(reservationPackage.getQuantity())
                            .price(reservationPackage.getPrice())
                            .optionFlg(reservationPackage.isOptionFlg())
                            .build()
            );
        }

        return myPageReservationPackageResponseList;
    }

}