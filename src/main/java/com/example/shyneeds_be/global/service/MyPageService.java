package com.example.shyneeds_be.global.service;

import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.model.dto.response.MyPageResponseDto;
import com.example.shyneeds_be.global.model.dto.response.MyPageUserResponseDto;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    public ApiResponseDto<MyPageResponseDto> getMyPageMain(Long id) {
        try{
            User user = userRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("없는 유저입니다."));

            MyPageResponseDto myPageResponse = myPageResponse(user);
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "마이페이지 데이터 불러오기 성공 ", myPageResponse);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "마이페이지 데이터 불러오기 실패 " + e.getMessage());
        }
    }

    private MyPageResponseDto myPageResponse(User user) {

        String imageDir = "https://shyneeds.s3.ap-northeast-2.amazonaws.com/user/"+
                user.getId().toString();
        return MyPageResponseDto.builder()
                .userInfo(
                        MyPageUserResponseDto.builder()
                                .profileImage(imageDir+"/"+ user.getProfileImage())
                                .email(user.getEmail())
                                .name(user.getName())
                                .birthday(user.getBirthday())
                                .gender(user.getGender())
                        .build())
                .reservationList(null)
                .build();

    }
/*
    public ApiResponseDto<MyPageResponseDto> getMyPageMain(Long id) {
       try{
           User user = userRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
           List<Reservation> reservationList = reservationRepository.findAllByUserId(user.getId());
           List<ReservationPackage> reservationPackageList = null;
           for (Reservation reservation : reservationList){
                reservationPackageList.add(reservationPackageRepository.findAllByReservationId(reservation.getId()));
           }

           MyPageResponseDto.builder()
                   .user(MyPageUserResponseDto.builder()
                           .email(user.getEmail())
                           .name(user.getName())
                           .birthday(user.getBirthday())
                           .gender(user.getGender())
                           .build())
                   .reservation(myReservationList)
                   .build();

           List<MyPageReservationResponseDto> myReservationList = null;

           MyPageReservationResponseDto.builder()
                   .reservationNumber(reservation.getReservationNumber())
                   .reservatedAt(reservation.getCreatedAt())
                   .reservationPackage(myReservationPackageList)
                   .reservationStatus(reservation.getReservationStatus())
                   .build();


           List<MyPageReservationPackageResponseDto> myReservationPackageList = null;

           MyPageReservationPackageResponseDto.builder()
                   .packageId()
                   .title()
                   .optionName()
                   .optionValue()
                   .price()
                   .build();

           return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "마이페이지 불러오기 성공", myPageResponse);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "마이페이지 불러오기 실패" + e.getMessage());
        }
    }
*/
}