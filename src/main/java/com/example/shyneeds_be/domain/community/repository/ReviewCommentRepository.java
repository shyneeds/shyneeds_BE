package com.example.shyneeds_be.domain.community.repository;

import com.example.shyneeds_be.domain.community.model.entity.ReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    // 리뷰 댓글 찾기 쿼리
    @Query(value = "SELECT * FROM shyneeds.review_comment " +
            "WHERE id = :commentId " +
            "AND deleted_flg = 0 ",
            nativeQuery = true)
    Optional<ReviewComment> findById(@Param("commentId") Long commentId);

    // 리뷰 댓글 찾기 쿼리
    @Query(value = "SELECT * FROM shyneeds.review_comment " +
            "WHERE id = :commentId AND user_id = :userId " +
            "AND deleted_flg = 0 ",
            nativeQuery = true)
    Optional<ReviewComment> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);


    // 리뷰의 댓글 리스트 찾기 쿼리
    @Query(value = "SELECT * FROM shyneeds.review_comment " +
            "WHERE review_id = :reviewId " +
            "AND deleted_flg = 0 " +
            "ORDER BY updated_at DESC"
            , nativeQuery = true)
    Page<ReviewComment> findByReviewId(@Param("reviewId") Long reviewId, Pageable pageable);
}
