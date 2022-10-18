package com.example.shyneeds_be.domain.reservation.model.entity;

import com.example.shyneeds_be.domain.reservation.model.enums.ReservationStatus;
import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Table(name = "reservation")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method")
    private String paymentMethod;  // 결제수단

    @Column(name = "payment_account_bank")
    private String paymentAccountBank;  // 결제 은행

    @Column(name = "payment_account_number")
    private String paymentAccountNumber;  // 계좌번호

    @Column(name = "payment_account_holder")
    private String paymentAccountHolder; // 예금주

    @Column(name = "total_reservation_amount")
    private String totalReservationAmount;  // 총 예약 금액

    @Column(name = "reservation_number")
    private String reservationNumber;  // 예약번호

    @Column(name = "depositor_name")
    private String depositorName;  // 입금자명

    @Column(name = "service_terms")
    private Boolean serviceTerms;  // 약관동의 여부

    @Column(name = "reservation_status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus; // 예약상태

    @Column(name = "reservator_name")
    private String reservatorName;

    @Column(name = "reservator_phone_number")
    private String reservatorPhoneNumber;

    @Column(name = "reservator_email")
    private String reservatorEmail;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @JsonBackReference
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationPackage> reservationPackages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
