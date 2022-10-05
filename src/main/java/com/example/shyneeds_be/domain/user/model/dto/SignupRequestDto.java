package com.example.shyneeds_be.domain.user.dto;

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
    private Date birthday;
    private String gender;
}
