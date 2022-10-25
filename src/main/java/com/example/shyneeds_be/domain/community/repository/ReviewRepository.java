package com.example.shyneeds_be.domain.community.repository;

import com.example.shyneeds_be.domain.community.model.entity.Review;
import com.example.shyneeds_be.domain.community.model.entity.ReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 후기 메인 페이지
    @Query(value = "SELECT * FROM shyneeds.review " +
            "WHERE " +
            "(CASE " +
            "WHEN :search = 'all' THEN  true " +
            "WHEN :search != 'all' THEN title REGEXP :search " +
            "END) " +
            "AND deleted_flg = 0 " +
            "ORDER BY updated_at DESC ",
            nativeQuery = true)
    Page<Review> findBySearch(@Param("search") String search, Pageable pageable);

    // MyPage 내가 작성한 리뷰
    @Query(value = "SELECT * FROM shyneeds.review " +
            "WHERE user_id = :userId " +
            "AND deleted_flg = 0 " +
            "ORDER BY updated_at DESC"
            , nativeQuery = true)
    Page<Review> findByUserId(@Param("userId") Long userId, Pageable pageable);

    // 리뷰 상세조회
    @Query(value = "SELECT * FROM shyneeds.review " +
            "WHERE id = :reviewId " +
            "AND deleted_flg = 0 "
    ,nativeQuery = true)
    Optional<Review> findById(@Param("reviewId") Long reviewId);


    // 예약 고유번호로 리뷰 찾기 - 방어로직. 동일 예약 건에 대해 한 번만 리뷰작성 가능
    @Query(value = "SELECT * FROM shyneeds.review " +
            "WHERE reservation_id = :reservationId " +
            "AND deleted_flg = 0"
    ,nativeQuery = true)
    Optional<Review> findByReservationId(@Param("reservationId") Long reservationId);


    // [메인] 베스트 후기 - 조회수 많은 순, 최신순
    @Query(value =  "SELECT * FROM shyneeds.review " +
            "WHERE " +
            "deleted_flg = 0 " +
            "ORDER BY lookup_count, updated_at DESC " +
            "LIMIT 10",
    nativeQuery = true)
    List<Review> findBestReviewList();

}
