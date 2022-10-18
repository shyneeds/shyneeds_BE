package com.example.shyneeds_be.domain.reservation.repository;

import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long id);

    Reservation findByReservationNumber(String reservationNumber);
}
