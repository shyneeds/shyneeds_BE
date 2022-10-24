package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.model.dto.response.ReviewMainResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewResponseDto;
import com.example.shyneeds_be.domain.community.service.ReviewService;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.global.auth.jwt.Auth;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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


    @Auth
    @ApiOperation(value = "회원이 작성한 리뷰 리스트 조회")
    @GetMapping("/mypage")
    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(HttpServletRequest request, @PageableDefault Pageable pageable){
        Long userId = (Long) request.getAttribute("userId");
        User user = User.builder()
                .id(userId)
                .build();
        return reviewService.getMyReviewList(user, pageable);
    }

}
