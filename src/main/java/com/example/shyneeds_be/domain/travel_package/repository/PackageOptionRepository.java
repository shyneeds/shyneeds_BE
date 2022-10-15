package com.example.shyneeds_be.domain.travel_package.repository;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.PackageOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageOptionRepository extends JpaRepository<PackageOption, Long> {
}
