package com.example.shyneeds_be.domain.community.service;

import com.example.shyneeds_be.domain.community.model.dto.response.ReviewMainResponseDto;
import com.example.shyneeds_be.domain.community.model.dto.response.ReviewResponseDto;
import com.example.shyneeds_be.domain.community.model.entity.Review;
import com.example.shyneeds_be.domain.community.repository.ReviewRepository;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.Pagination;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;


    // 리뷰 리스트 메인 (검색 포함. all 전체 검색)
    public ApiResponseDto<List<ReviewMainResponseDto>> getReviewList(String search, Pageable pageable) {
        try{

            Page<Review> reviewList = reviewRepository.findBySearch(search, pageable);
            Pagination pagination = Pagination.getPagination(reviewList);

            List<ReviewMainResponseDto> reviewMainResponseDtoList = reviewList.map(this::response).toList();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewMainResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }

    private ReviewMainResponseDto response(Review review) {

        // user name 마스킹 (김**) 처리
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(review.getUserId()));
        String author = "탈퇴회원";
        if(optionalUser.isPresent()){
            author = getMaskingAuthor(optionalUser.get().getName());
        }
        return ReviewMainResponseDto.builder()
                .id(review.getId())
                .mainImage(review.getMainImage())
                .title(review.getTitle())
                .updatedAt(review.getUpdatedAt())
                .author(author)
                .build();
    }

    // 김** 처리
    private String getMaskingAuthor(String username){
        StringBuilder sb = new StringBuilder();
        char[] userNameCharList = username.toCharArray();
        sb.append(userNameCharList[0]);
        for (int i = 0; i < userNameCharList.length-1; i++) {
            sb.append("*");
        }

        return sb.toString();
    }

    // [Mypage] 내가 작성한 리뷰 조회
    public ApiResponseDto<List<ReviewMainResponseDto>> getMyReviewList(User user, Pageable pageable) {
        try{

            Page<Review> userReviewList = reviewRepository.findByUserId(user.getId(), pageable);

            Pagination pagination = Pagination.getPagination(userReviewList);

            List<ReviewMainResponseDto> reviewMainResponseDtoList = userReviewList.map(this::response).stream().toList();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "조회에 성공했습니다.", reviewMainResponseDtoList, pagination);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "조회에 실패했습니다. " + e.getMessage());
        }
    }
}
