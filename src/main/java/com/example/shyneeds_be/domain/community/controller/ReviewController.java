package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.model.dto.request.ReviewRegisterRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.request.ReviewUpdateRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewMainResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewResponseDto;
import com.example.shyneeds_be.domain.community.service.ReviewService;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponseDto<List<ReviewMainResponseDto>> getReviewList(@PathParam("search") String search, @PageableDefault(value = 12) Pageable pageable) {
        return reviewService.getReviewList(search, pageable);
    }


    @ApiOperation(value = "리뷰 저장")
    @PostMapping("/register")
    public ApiResponseDto registerReview(@AuthenticationPrincipal User user, @RequestBody ReviewRegisterRequestDto reviewRegisterRequestDto) {
        return reviewService.register(user, reviewRegisterRequestDto);
    }

    @ApiOperation(value = "리뷰 이미지 저장 (단일 파일 -> url 리턴)")
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String image(@RequestPart("upload") MultipartFile upload) throws Exception {
        return reviewService.saveImage(upload);
    }


    @ApiOperation(value = "리뷰 상세 조회")
    @GetMapping("/{id}/details")
    public ApiResponseDto<ReviewResponseDto> getReview(@AuthenticationPrincipal User user, @PathVariable("id") Long reviewId){
        return reviewService.getReview(user, reviewId);
    }


    @ApiOperation(value = "리뷰 수정")
    @PutMapping("/update")
    public ApiResponseDto updateReview(@AuthenticationPrincipal User user, @RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto) {
        return reviewService.updateReview(user, reviewUpdateRequestDto);
    }

    @ApiOperation(value = "리뷰 삭제")
    @DeleteMapping("/{id}")
    public ApiResponseDto deleteReview(@AuthenticationPrincipal User user, @PathVariable("id") Long reviewId) {
        return reviewService.deleteReview(user, reviewId);
    }



    @ApiOperation(value = "회원이 작성한 리뷰 리스트 조회")
    @GetMapping("/mypage")

    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(@AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
        return reviewService.getMyReviewList(user, pageable);
    }

}
