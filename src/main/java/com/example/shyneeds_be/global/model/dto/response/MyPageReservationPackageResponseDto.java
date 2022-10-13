package com.example.shyneeds_be.global.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MyPageReservationPackageResponseDto {

    private Long packageId;

    private String title;

    private String optionName;

    private String optionValue;

    private String price;
}
