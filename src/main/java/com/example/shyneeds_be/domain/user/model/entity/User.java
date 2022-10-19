package com.example.shyneeds_be.domain.user.model.entity;

import com.example.shyneeds_be.domain.cart.model.entity.Cart;
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
import java.util.Date;
import java.util.List;

@Table(name = "user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "role")
    private String role;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "total_payment_amount")
    private Long totalPaymentAmount;

    @Column(name = "profile_image")
    private String profileImage;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToOne
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;

    @JsonBackReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts;

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

    public void saveRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
