package com.example.shyneeds_be.domain.travel_package.repository;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {

    @Query(value="SELECT * FROM shyneeds.travel_package " +
            "WHERE id = :id " +
            "AND deleted_flg = 0" , nativeQuery = true)
    Optional<TravelPackage> findById(@Param("id") Long id);
}
