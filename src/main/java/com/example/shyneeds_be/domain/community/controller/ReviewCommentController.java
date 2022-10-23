package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.model.dto.request.ReviewCommentRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.request.ReviewCommentUpdateRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewCommentResponseDto;
import com.example.shyneeds_be.domain.community.service.ReviewCommentService;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ReviewCommentController {


    private final ReviewCommentService reviewCommentService;


    @ApiOperation(value = "댓글 등록하기")
    @PostMapping("/register")
    public ApiResponseDto register(@RequestBody ReviewCommentRequestDto reviewCommentRequestDto) {
        User user = User.builder().id(30L).build();
        return reviewCommentService.register(user, reviewCommentRequestDto);
    }

    @ApiOperation(value = "리뷰의 댓글 불러오기")
    @GetMapping("/{id}/list")
    public ApiResponseDto<List<ReviewCommentResponseDto>> getCommentList(@PathVariable("id") Long reviewId, @PageableDefault Pageable pageable){
        return reviewCommentService.getCommentList(reviewId, pageable);
    }

    @ApiOperation(value = "댓글 불러오기")
    @GetMapping("/{id}")
    public ApiResponseDto<ReviewCommentResponseDto> getComment(@PathVariable("id") Long commentId){
        User user = User.builder().id(30L).build();
        return reviewCommentService.getComment(user, commentId);
    }


    @ApiOperation(value = "댓글 수정하기")
    @PutMapping("/update")
    public ApiResponseDto updateComment(@RequestBody ReviewCommentUpdateRequestDto reviewCommentUpdateRequestDto){
        User user = User.builder().id(30L).build();
        return reviewCommentService.updateComment(user, reviewCommentUpdateRequestDto);
    }

    @ApiOperation(value = "댓글 삭제하기")
    @DeleteMapping("/{id}")
    public ApiResponseDto deleteComment(@PathVariable("id") Long commentId) {
        User user = User.builder().id(30L).build();
        return reviewCommentService.deleteComment(user, commentId);
    }

}
