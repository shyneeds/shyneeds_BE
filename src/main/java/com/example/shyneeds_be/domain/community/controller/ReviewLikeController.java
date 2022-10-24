package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.service.ReviewLikeService;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.global.auth.jwt.Auth;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @Auth
    @ApiOperation(value = "좋아요", notes = "회원이 한 번 누르면 좋아요, 한 번 더 누르면 좋아요 취소")
    @PostMapping("/like")
    public ApiResponseDto<Boolean> like(HttpServletRequest request, @RequestParam(value = "review_id") Long reviewId){
        User user = User.builder().id((Long) request.getAttribute("userId")).build();
        return reviewLikeService.likeOrUnLike(user, reviewId);
    }



}
