package com.example.shyneeds_be.domain.community.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 메인 화면 베스트 리뷰
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BestReviewResponseDto {

    @ApiModelProperty(name = "리뷰 고유번호", required = true,  example = "")
    private Long id;

    @ApiModelProperty(name = "메인 이미지", required = true,  example = "")
    private String mainImage;

    @ApiModelProperty(name = "타이틀", required = true,  example = "")
    private String title;

    @ApiModelProperty(name = "컨텐츠", required = true,  example = "")
    private String contents;

    @ApiModelProperty(name = "수정일", required = true,  example = "")
    private Timestamp updatedAt;

    @ApiModelProperty(name = "작성자", required = true,  example = "")
    private String author;
}
