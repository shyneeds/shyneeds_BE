package com.example.shyneeds_be.domain.community.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommentRequestDto {


    @ApiModelProperty(value = "리뷰 고유번호", required = true, example = "")
    private Long reviewId;

    @ApiModelProperty(value = "댓글 내용", required = true, example = "")
    private String comment;
}
