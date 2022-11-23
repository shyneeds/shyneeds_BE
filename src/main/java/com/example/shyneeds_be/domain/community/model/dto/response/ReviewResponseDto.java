package com.example.shyneeds_be.domain.community.model.dto.response;

import com.example.shyneeds_be.domain.community.model.entity.VisitPackage;
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
public class ReviewResponseDto {

    @ApiModelProperty(name = "후기 고유번호", required = false,  example = "")
    private Long id;

    @ApiModelProperty(name = "타이틀", required = false,  example = "")
    private String title;

    @ApiModelProperty(name = "후기 메인 이미지", required = false,  example = "")
    private String mainImage;

    @ApiModelProperty(name = "예약 고유번호", required = false,  example = "")
    private Long reservationId;

    @ApiModelProperty(name = "후기 수정일", required = false,  example = "")
    private Timestamp updatedAt;

    @ApiModelProperty(name = "후기 작성자", required = false,  example = "")
    private String author;

    @ApiModelProperty(name = "조회수", required = false,  example = "")
    private int lookupCount;

    @ApiModelProperty(name = "좋아요 갯수", required = false,  example = "")
    private int likeCount;

    @ApiModelProperty(name = "로그인 한 회원의 좋아요 여부", required = false,  example = "")
    private boolean isLike;

    @ApiModelProperty(name = "후기 내용(contents)", required = false,  example = "")
    private String contents;

    @ApiModelProperty(name = "다녀온 상품 정보", required = false,  example = "")
    private VisitPackageResponseDto visitPackageResponseDto;

    // 댓글은 따로 호출한다.
}
