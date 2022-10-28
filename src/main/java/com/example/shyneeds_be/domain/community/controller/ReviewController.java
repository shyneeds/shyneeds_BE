package com.example.shyneeds_be.domain.community.controller;

import com.example.shyneeds_be.domain.community.model.dto.request.ReviewRegisterRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.request.ReviewUpdateRequestDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewMainResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewResponseDto;
import com.example.shyneeds_be.domain.community.service.ReviewService;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.global.auth.jwt.Auth;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

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


    @Auth
    @ApiOperation(value = "리뷰 저장")
    @PostMapping("/register")
    public ApiResponseDto registerReview(HttpServletRequest request, @RequestBody ReviewRegisterRequestDto reviewRegisterRequestDto) {
        User user = User.builder().id((Long) request.getAttribute("userId")).build();
        return reviewService.register(user, reviewRegisterRequestDto);
    }

    @ApiOperation(value = "리뷰 이미지 저장 (단일 파일 -> url 리턴)")
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String image(@RequestPart("upload") MultipartFile upload) throws Exception {

        return reviewService.saveImage(upload);
    }


    @Auth
    @ApiOperation(value = "리뷰 상세 조회")
    @GetMapping("/{id}/details")
    public ApiResponseDto<ReviewResponseDto> getReview(HttpServletRequest request, @PathVariable("id") Long reviewId){
        User user = User.builder().id((Long) request.getAttribute("userId")).build();
        return reviewService.getReview(user, reviewId);
    }


    @Auth
    @ApiOperation(value = "리뷰 수정")
    @PutMapping("/update")
    public ApiResponseDto updateReview(HttpServletRequest request, @RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto) {
        User user = User.builder().id((Long) request.getAttribute("userId")).build();
        return reviewService.updateReview(user, reviewUpdateRequestDto);
    }

    @Auth
    @ApiOperation(value = "리뷰 삭제")
    @DeleteMapping("/{id}")
    public ApiResponseDto deleteReview(HttpServletRequest request, @PathVariable("id") Long reviewId) {
        User user = User.builder().id((Long) request.getAttribute("userId")).build();
        return reviewService.deleteReview(user, reviewId);
    }



    @Auth
    @ApiOperation(value = "회원이 작성한 리뷰 리스트 조회")
    @GetMapping("/mypage")

    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(HttpServletRequest request, @PageableDefault Pageable pageable) {
        Long userId = (Long) request.getAttribute("userId");
        User user = User.builder()
                .id(userId)
                .build();
        return reviewService.getMyReviewList(user, pageable);
    }

}
