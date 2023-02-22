package com.example.shyneeds_be.domain.cart.model.entity;

import com.example.shyneeds_be.domain.travel_package.model.entitiy.TravelPackage;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "cart")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Cart { // 관심상품

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_title")
    private String optionTitle;

    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private String price;

    @Column(name = "option_flg")
    private boolean optionFlg;

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
    @JoinColumn(name = "member_id")
    private Member member;

}
