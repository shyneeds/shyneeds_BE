package com.example.shyneeds_be.domain.user.model.entity;

import com.example.shyneeds_be.domain.cart.model.entity.Cart;
import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "role")
    private String role;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "profile_image")
    private String profileImage;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    @JsonBackReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts;

    public void saveRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void updateInfo(String password, String name, Date birthday, String gender) {
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateInfoNoPass(String name, Date birthday, String gender){
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateInfoWithImage(String profileImageUrl, String password, String name, Date birthday, String gender) {
        this.profileImage = profileImageUrl;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateInfoNoPassWithImage(String profileImageUrl,String name, Date birthday, String gender) {
        this.profileImage = profileImageUrl;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }
}
