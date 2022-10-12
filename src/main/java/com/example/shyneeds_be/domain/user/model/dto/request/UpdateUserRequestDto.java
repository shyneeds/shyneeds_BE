package com.example.shyneeds_be.domain.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateUserRequestDto {
    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String year;

    private String month;

    private String day;

    private String gender;
}
