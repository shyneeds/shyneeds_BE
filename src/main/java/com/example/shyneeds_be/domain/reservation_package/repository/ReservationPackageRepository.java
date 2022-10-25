package com.example.shyneeds_be.domain.reservation_package.repository;

import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationPackageRepository extends JpaRepository <ReservationPackage, Long> {
    List<ReservationPackage> findAllByReservationId(Long id);
}
