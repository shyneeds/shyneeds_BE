package com.example.shyneeds_be.global.security.oauth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ValidateRefreshRequestDto {
    private String refreshToken;
    private String accessToken;
}
