package com.example.shyneeds_be.global.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecreatedAccessTokenResponseDto {
    private String accessToken;
}
