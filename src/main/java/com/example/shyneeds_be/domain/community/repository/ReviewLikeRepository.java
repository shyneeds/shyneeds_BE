package com.example.shyneeds_be.domain.community.repository;

import com.example.shyneeds_be.domain.community.model.entity.ReviewLike;
import com.example.shyneeds_be.domain.community.model.entity.ReviewLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    //SELECT user_id, review_id, count(*)%2 FROM shyneeds.review_like GROUP BY user_id
    // 해당 리뷰의 좋아요 갯수
    @Query(value = "SELECT COUNT(*)%2 AS cnt FROM shyneeds.review_like " +
            "WHERE review_id = :reviewId " +
            "GROUP BY user_id",
            nativeQuery = true)
    List<ReviewLikeCount> findByReviewLike(@Param("reviewId") Long reviewId);


    // 해당 리뷰에 로그인 유저의 좋아요 여부 확인 쿼리
    @Query(value = "SELECT * FROM shyneeds.review_like " +
            "WHERE user_id = :userId AND review_id = :reviewId " +
            "ORDER BY created_at DESC " +
            "LIMIT 1"
    ,nativeQuery = true)
    Optional<ReviewLike> findIsLike(@Param("userId") Long userId, @Param("reviewId") Long reviewId);
}
