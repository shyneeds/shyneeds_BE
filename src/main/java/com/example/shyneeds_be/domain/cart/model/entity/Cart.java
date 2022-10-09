package com.example.shyneeds_be.domain.cart.model.entity;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "cart")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Cart { // 관심상품

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

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_package_id")
    private TravelPackage travelPackage;

}
