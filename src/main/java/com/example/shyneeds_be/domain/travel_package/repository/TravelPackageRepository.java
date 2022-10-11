package com.example.shyneeds_be.domain.travel_package.repository;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.global.model.dto.response.MainTravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

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
    @Query(value = "SELECT " +
            "CA.title AS categoryTitle, " +
            "SCA.title AS subCategoryTitle, " + // sub category title => tag로 이용
            "TP.id AS id, " +
            "TP.title AS title, " +
            "TP.main_image AS mainImage, " +
            "TP.summary AS summary, " +
            "TP.price AS price, " +
            "TP.search_keyword AS keyword " +
            "FROM shyneeds.travel_package AS TP " +
            "INNER JOIN shyneeds.category AS CA " +
            "ON CA.title REGEXP :searchTitle " +
            "INNER JOIN shyneeds.sub_category AS SCA ON SCA.category_id = CA.id " +
            "WHERE TP.sub_category_ids REGEXP (SCA.id) " +
            "GROUP BY TP.id " +
            "ORDER BY TP.updated_at DESC"
            ,nativeQuery = true)
    List<MainTravelPackage> findByCategory(@Param(value = "searchTitle") String category);


    // 큐레이션 검색
    @Query(value = "SELECT " +
            "SCA.title AS categoryTitle, " + // sub category id -> badge
            "TP.id AS id, " +
            "TP.title AS title, " +
            "TP.main_image AS mainImage, " +
            "TP.summary AS summary, " +
            "TP.price AS price, " +
            "TP.search_keyword AS keyword " +
            "FROM shyneeds.sub_category AS SCA " +
            "INNER JOIN shyneeds.travel_package AS TP ON TP.sub_category_ids REGEXP SCA.id " +
            "INNER JOIN shyneeds.sub_category AS SCA2 ON TP.sub_category_ids REGEXP SCA2.id " +
            "INNER JOIN shyneeds.sub_category AS SCA3 ON TP.sub_category_ids REGEXP SCA3.id " +
            "INNER JOIN shyneeds.sub_category AS SCA4 ON TP.sub_category_ids REGEXP SCA4.id " +
            "INNER JOIN shyneeds.sub_category AS SCA5 ON TP.sub_category_ids REGEXP SCA5.id " +
            "INNER JOIN shyneeds.sub_category AS SCA6 ON TP.sub_category_ids REGEXP SCA6.id " +
            "WHERE " +
            "IF(:ageGroup IS NOT NULL, SCA.title REGEXP :ageGroup, true) " +
            "AND IF(:accompanyGroup IS NOT NULL, SCA2.title REGEXP :accompanyGroup, true) " +
            "AND IF(:tendencyGroup IS NOT NULL, SCA3.title REGEXP :tendencyGroup, true) " +
            "AND IF(:religionGroup IS NOT NULL, SCA4.title REGEXP :religionGroup, true) " +
            "AND IF(:countryGroup IS NOT NULL, SCA5.title REGEXP :countryGroup, true) " +
            "AND IF(:themeGroup IS NOT NULL, SCA5.title REGEXP :themeGroup, true) " +
            "AND TP.disp_flg = 1 AND TP.deleted_flg = 0 " +
            "GROUP BY TP.id " +
            "ORDER BY TP.updated_at DESC"
            ,nativeQuery = true)
    List<MainTravelPackage> findByCuration(
            @Param(value = "ageGroup") String ageGroup, @Param(value = "accompanyGroup") String accompanyGroup,
            @Param(value = "tendencyGroup") String tendencyGroup, @Param(value = "religionGroup") String religionGroup,
            @Param(value = "countryGroup") String countryGroup, @Param(value = "themeGroup") String themeGroup
            );
}
