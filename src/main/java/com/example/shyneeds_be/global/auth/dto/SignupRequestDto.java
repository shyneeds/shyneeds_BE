package com.example.shyneeds_be.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private String name;
    private String year;
    private String month;
    private String day;
    private String gender;
}
