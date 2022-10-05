package com.example.shyneeds_be.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JoinRequestDto {
    private String email;
    private String password;
    private String userName;
    private String birthDay;
    private String gender;
}
