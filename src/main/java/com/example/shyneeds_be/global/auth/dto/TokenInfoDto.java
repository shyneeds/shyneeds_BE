package com.example.shyneeds_be.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfoDto {
    private String accessToken;
    private String refreshToken;
    private String keyEmail;

    public TokenInfoDto(String accessToken){
        this.accessToken = accessToken;
    }

}
