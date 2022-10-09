package com.example.shyneeds_be.domain.reservation_package.model.entity;

import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import com.example.shyneeds_be.domain.reservation_package.model.enums.PackageReservationStatus;
import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "reservation_package")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ReservationPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "select_required_option_name")
    private String selectRequiredOptionName;

    @Column(name = "select_required_option_values")
    private  String selectRequiredOptionValues;

    @Column(name = "select_optional_name")
    private String selectOptionalName;

    @Column(name = "select_optional_values")
    private String selectOptionalValues;

    @Column(name = "reservation_price")
    private Long reservation_price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "package_reservation_status")
    private PackageReservationStatus packageReservationStatus;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_package_id")
    private TravelPackage travelPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
