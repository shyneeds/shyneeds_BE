package com.example.shyneeds_be.domain.community.repository;

import com.example.shyneeds_be.domain.community.model.entity.Review;
import com.example.shyneeds_be.domain.community.model.entity.ReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
