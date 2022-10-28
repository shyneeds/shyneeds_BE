package com.example.shyneeds_be.domain.travel_package.repository;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.GroupPackage;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupPackageRepository extends JpaRepository<TravelPackage, Long> {

    // [그룹별여행] 메인 카테고리 (그룹별) 여행 리스트 출력 쿼리
    @Query(value = "SELECT " +
            "TP.id AS travelId, TP.title AS travelTitle, TP.price AS price, TP.main_image AS mainImage, TP.search_keyword AS searchKeyword, " +
            "SCA.id AS subCategoryId, SCA.title AS subCategoryTitle " +
            "FROM shyneeds.travel_package AS TP " +
            "INNER JOIN shyneeds.sub_category AS SCA " +
            "ON FIND_IN_SET(SCA.id, TP.sub_category_ids) " +
            "WHERE " +
            "SCA.category_id = (SELECT id FROM shyneeds.category WHERE title REGEXP :name) " +
            "AND soldout_flg = 0 AND deleted_flg = 0 " +
            "ORDER BY TP.updated_at DESC "
            ,nativeQuery = true)
    List<GroupPackage> findGroupPackage(@Param("name") String name);

    // [그룹별여행] 메인 카테고리 (그룹별) 여행 리스트 출력 쿼리 (전체보기)
    @Query(value = "SELECT TP.id AS travelId, TP.title AS travelTitle, TP.price AS price, TP.main_image AS mainImage, TP.search_keyword AS searchKeyword, " +
            "SCA.id AS subCategoryId, SCA.title AS subCategoryTitle " +
            "FROM shyneeds.travel_package AS TP " +
            "INNER JOIN shyneeds.sub_category AS SCA " +
            "ON SCA.title = (SELECT title FROM shyneeds.sub_category WHERE title REGEXP :name) " +
            "WHERE " +
            "FIND_IN_SET( (SELECT id FROM shyneeds.sub_category WHERE title REGEXP :name), TP.sub_category_ids )" +
            "AND " +
            "soldout_flg = 0 AND deleted_flg = 0 " +
            "GROUP BY TP.id " +
            "ORDER BY " +
            "(CASE WHEN :sortFlg='current' THEN TP.updated_at END) DESC," +
            "(CASE WHEN :sortFlg='high' THEN TP.price END) DESC, " +
            "(CASE WHEN :sortFlg='low' THEN TP.price END) ASC"
            ,nativeQuery = true)
    List<GroupPackage> findBySubTitle(@Param("name") String name, @Param("sortFlg") String sortFlg);

}
