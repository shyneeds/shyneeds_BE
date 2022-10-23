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
public class ReviewCommentUpdateRequestDto {


    @ApiModelProperty(value = "댓글 고유번호", required = true, example = "")
    private Long commentId;

    @ApiModelProperty(value = "댓글 내용", required = true, example = "")
    private String comment;


}
