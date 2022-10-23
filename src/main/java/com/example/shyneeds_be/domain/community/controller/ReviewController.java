package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.model.dto.response.ReviewMainResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewResponseDto;
import com.example.shyneeds_be.domain.community.service.ReviewService;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 리스트", notes = "[검색] search  = all or title")
    @GetMapping("")
    public ApiResponseDto<List<ReviewMainResponseDto>> getReviewList(@PathParam("search") String search, @PageableDefault(value = 12)Pageable pageable){
        return reviewService.getReviewList(search, pageable);
    }


    @ApiOperation(value = "회원이 작성한 리뷰 리스트 조회")
    @GetMapping("/mypage")
    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(@PageableDefault Pageable pageable){
        User user = User.builder().id(30L).build();
        return reviewService.getMyReviewList(user, pageable);
    }

}
