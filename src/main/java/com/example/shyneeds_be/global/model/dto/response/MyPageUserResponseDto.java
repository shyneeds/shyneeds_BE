package com.example.shyneeds_be.global.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MyPageUserResponseDto {

    private String profileImage;

    private String email;

    private String name;

    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date birthday;

    private String gender;

    private Long totalPaymentAmount;
}
