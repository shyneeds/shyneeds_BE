package com.example.shyneeds_be.domain.reservation.repository;

import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
