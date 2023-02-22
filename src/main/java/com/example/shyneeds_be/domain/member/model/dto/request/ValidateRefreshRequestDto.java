package com.example.shyneeds_be.domain.member.model.dto.request;

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
