package com.example.shyneeds_be.global.security.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignupRequestDto {

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String year;

    private String month;

    private String day;

    private String gender;
}
