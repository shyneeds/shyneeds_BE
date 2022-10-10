package com.example.shyneeds_be.domain.travel_package.repository;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.global.model.dto.response.MainTravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {

    @Query(value="SELECT * FROM shyneeds.travel_package " +
            "WHERE id = :id " +
            "AND deleted_flg = 0" , nativeQuery = true)
    Optional<TravelPackage> findById(@Param("id") Long id);


    // 메인 배너 리스트
    @Query(value ="SELECT * FROM shyneeds.travel_package " +
            "WHERE main_banner_flg = 1 " +
            "AND deleted_flg = 0", nativeQuery = true)
    List<TravelPackage> findByMainBanner();

    // 카테고리 별 여행 상품 리스트 출력
    @Query(value = "SELECT CA.title AS categoryTitle, " +
            "TP.id AS id, " +
            "TP.title AS title, " +
            "TP.main_image AS mainImage, " +
            "TP.summary AS summary, " +
            "TP.price AS price, " +
            "TP.search_keyword AS keyword " +
            "FROM shyneeds.travel_package AS TP " +
            "INNER JOIN shyneeds.category AS CA " +
            "ON CA.title LIKE %:searchTitle% " +
//            "INNER JOIN shyneeds.sub_category AS SCA " +
//            "ON SCA."
            "WHERE TP.category_ids IN (CA.id)"
            ,nativeQuery = true)
    List<MainTravelPackage> findByCategory(@Param(value = "searchTitle") String category);

    @Query(value="SELECT * FROM shyneeds.travel_package " +
            "WHERE id = :id ", nativeQuery = true)
    TravelPackage findByPackageId(@Param("id") Long id);
}
