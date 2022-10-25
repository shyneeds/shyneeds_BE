package com.example.shyneeds_be.domain.reservation.repository;

import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long id);

    Reservation findByReservationNumber(String reservationNumber);

    @Query(value = "SELECT * FROM shyneeds.reservation " +
            "WHERE user_id = :userId AND id = :reservationId " +
            "AND reservation_status = '예약확정' "
    ,nativeQuery = true)
    Optional<Reservation> findByUserIdAndReservationId(@Param("userId") Long userId, @Param("reservationId") Long reservationId);
}
