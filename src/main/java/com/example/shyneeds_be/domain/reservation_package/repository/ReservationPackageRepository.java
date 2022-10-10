package com.example.shyneeds_be.domain.reservation_package.repository;

import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationPackageRepository extends JpaRepository <ReservationPackage, Long> {
}
