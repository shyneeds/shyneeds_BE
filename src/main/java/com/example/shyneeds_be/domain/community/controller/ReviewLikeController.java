package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.service.ReviewLikeService;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;


    @ApiOperation(value = "좋아요", notes = "회원이 한 번 누르면 좋아요, 한 번 더 누르면 좋아요 취소")
    @PostMapping("/like")
    public ApiResponseDto<Boolean> like(@AuthenticationPrincipal User user, @RequestParam(value = "review_id") Long reviewId){
        return reviewLikeService.likeOrUnLike(user, reviewId);
    }



}
