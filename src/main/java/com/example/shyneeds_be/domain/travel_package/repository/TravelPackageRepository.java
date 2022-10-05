package com.example.shyneeds_be.domain.travel_package.repository;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
}
