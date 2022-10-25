package com.example.shyneeds_be.domain.community.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommentResponseDto {

    @ApiModelProperty(value = "댓글 고유번호", required = true, example = "")
    private Long id;

    @ApiModelProperty(value = "회원 고유번호", required = true, example = "")
    private Long userId;

    @ApiModelProperty(value = "리뷰 고유번호", required = true, example = "")
    private Long reviewId;

    @ApiModelProperty(value = "회원 아이디", required = true, example = "")
    private String userName;

    @ApiModelProperty(value = "댓글 내용", required = true, example = "")
    private String comment;

    @ApiModelProperty(value = "수정일", required = true, example = "")
    private Timestamp updatedAt;

//    @ApiModelProperty(value = "대댓글. 0(본댓글), 1(대댓글)", required = true, example = "")
//    private int recomment;
}
