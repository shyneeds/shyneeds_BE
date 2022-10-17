package com.example.shyneeds_be.domain.user.service;

import com.example.shyneeds_be.domain.user.model.dto.request.UpdateUserRequestDto;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.network.s3.ItemS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ItemS3Uploader itemS3Uploader;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /*
        유저 정보 수정
    */
    @Transactional
    public ApiResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequest, MultipartFile profileImage) {
        try{
            User user = findUserById(id);

            String strBirthday = updateUserRequest.getYear() + "-" + updateUserRequest.getMonth() + "-" + updateUserRequest.getDay();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = dateFormat.parse(strBirthday);

            String profileImageUrl = uploadS3UserProfileImage(profileImage, id.toString());

            if(profileImage != null) {
                if (updateUserRequest.getPassword() != null) {
                    String password = passwordEncoder.encode(updateUserRequest.getPassword());
                    user.updateInfoWithImage(profileImageUrl, password, updateUserRequest.getName(),updateUserRequest.getPhoneNumber() ,birthday, updateUserRequest.getGender());
                }
                user.updateInfoNoPassWithImage(profileImageUrl, updateUserRequest.getName(), updateUserRequest.getPhoneNumber(), birthday, updateUserRequest.getGender());
            } else{
                if (updateUserRequest.getPassword() != null) {
                    String password = passwordEncoder.encode(updateUserRequest.getPassword());
                    user.updateInfo(password, updateUserRequest.getName(), updateUserRequest.getPhoneNumber(), birthday, updateUserRequest.getGender());
                }
                user.updateInfoNoPass(updateUserRequest.getName(), updateUserRequest.getPhoneNumber(), birthday, updateUserRequest.getGender());
            }
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "사용자 정보 수정에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "사용자 정보 수정에 실패했습니다." + e.getMessage());
        }
    }

    /*
        회원 탈퇴
    */
    public ApiResponseDto deleteUser(Long id) {
        try {
            userRepository.delete(findUserById(id));
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "회원탈퇴를 성공했습니다.");

        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "회원탈퇴에 실패했습니다.");
        }

    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("없는 유저입니다.") );
    }

    public String uploadS3UserProfileImage(MultipartFile profileImage, String bucketDir){
        return itemS3Uploader.uploadLocal(profileImage, bucket+"/user/"+bucketDir);
    }


}
