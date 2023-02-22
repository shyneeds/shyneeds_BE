package com.example.shyneeds_be.domain.member.model.entity;

import com.example.shyneeds_be.domain.cart.model.entity.Cart;
import com.example.shyneeds_be.domain.reservation.model.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Table(name = "member")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "member_name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "total_payment_amount")
    @ColumnDefault("0L")
    private Long totalPaymentAmount;

    @Column(name = "profile_image")
    private String profileImage;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @JsonBackReference
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservationList;

    @JsonBackReference
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> cartList;

    public void updateInfo(String password, String name, String phoneNumber, Date birthday, String gender) {
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateInfoNoPass(String name, String phoneNumber, Date birthday, String gender){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateInfoWithImage(String profileImageUrl, String password, String name, String phoneNumber, Date birthday, String gender) {
        this.profileImage = profileImageUrl;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateInfoNoPassWithImage(String profileImageUrl,String name, String phoneNumber, Date birthday, String gender) {
        this.profileImage = profileImageUrl;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
    }

}
