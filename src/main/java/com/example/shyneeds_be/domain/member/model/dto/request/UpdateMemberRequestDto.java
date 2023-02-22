package com.example.shyneeds_be.domain.member.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateMemberRequestDto {

    @ApiModelProperty(value = "예약할 패키지 리스트", required = true)
    private String password;

    @ApiModelProperty(value = "이름", required = true)
    private String name;

    @ApiModelProperty(value = "핸드폰 번호", required = true)
    private String phoneNumber;

    @ApiModelProperty(value = "출생년도", required = true, example = "1950")
    private String year;

    @ApiModelProperty(value = "출생월", required = true, example = "10")
    private String month;

    @ApiModelProperty(value = "출생일", required = true, example = "10")
    private String day;

    @ApiModelProperty(value = "성별", required = true, example = "female")
    private String gender;
}
