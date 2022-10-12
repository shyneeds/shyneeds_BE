package com.example.shyneeds_be.domain.user.service;

import com.example.shyneeds_be.domain.user.model.dto.request.UpdateUserRequestDto;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequest) {
        try{
            User user = findUserById(id);

            String strBirthday = updateUserRequest.getYear()+"-"+updateUserRequest.getMonth()+"-"+updateUserRequest.getDay();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = dateFormat.parse(strBirthday);

            user.builder()
                    .password(passwordEncoder.encode(user.getPassword()))
                    .name(updateUserRequest.getName())
                    .birthday(birthday)
                    .gender(updateUserRequest.getGender())
                    .build();
            userRepository.save(user);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "사용자 정보 수정에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "사용자 정보 수정에 실패했습니다." + e.getMessage());
        }
    }

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
}
