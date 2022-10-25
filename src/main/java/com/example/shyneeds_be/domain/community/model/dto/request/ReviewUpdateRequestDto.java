package com.example.shyneeds_be.domain.community.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequestDto {

    @ApiModelProperty(value = "리뷰 고유번호", required = false, example = "")
    private Long id;

    @ApiModelProperty(value = "리뷰 타이틀", required = false, example = "")
    private String title;

    @ApiModelProperty(value = "메인 이미지 url", required = false, example = "")
    private String mainImage;

    @ApiModelProperty(value = "콘텐츠 내용 (html 태그 포함)", required = false, example = "")
    private String contents;

    @ApiModelProperty(value = "예약 고유번호", required = false, example = "")
    private Long reservationId;

//    @ApiModelProperty(value = "삭제된 이미지 url 리스트 (이미지 수정 시 이용)", required = false, example = "")
//    private List<String> deletedImageList;
}
