package com.example.shyneeds_be.domain.member.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthRequestDto {
    private String accessToken;
}
