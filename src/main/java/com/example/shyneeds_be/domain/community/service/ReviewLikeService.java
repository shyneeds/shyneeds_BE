package com.example.shyneeds_be.domain.community.service;

import com.example.shyneeds_be.domain.community.model.entity.ReviewLike;
import com.example.shyneeds_be.domain.community.repository.ReviewLikeRepository;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    // 사용자 정보로 한 번 누르면 좋아요, 한 번 더 누르면 좋아요 취소
    public ApiResponseDto<Boolean> likeOrUnLike(Member member, Long reviewId){
        try{

            Boolean isLike = null;

            // 리뷰의 좋아요 검색
            Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.findIsLike(member.getId(), reviewId);
            // 리뷰의 좋아요가 없으면 좋아요 (true)
            if(optionalReviewLike.isEmpty()){
                isLike = true;
            } else {
                // 리뷰의 좋아요가 있으면서
                ReviewLike reviewLike = optionalReviewLike.get();
                // 리뷰의 좋아요 플래그가 true이면 false로, false이면 true로 변경
                if(reviewLike.isLikeFlg()){
                    isLike = false;
                } else{
                    isLike = true;
                }

            }

            ReviewLike like = ReviewLike.builder()
                    .userId(member.getId())
                    .reviewId(reviewId)
                    .likeFlg(isLike)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            ReviewLike saveLike = reviewLikeRepository.save(like);

            boolean likeFlg = saveLike.isLikeFlg();

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "좋아요 기능을 수행했습니다.", likeFlg);
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "좋아요 기능을 수행할 수 없습니다. " + e.getMessage());
        }
    }

}
