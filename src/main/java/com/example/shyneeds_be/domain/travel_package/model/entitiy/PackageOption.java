package com.example.shyneeds_be.domain.travel_package.model.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "package_option")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_package_id")
    private TravelPackage travelPackage;

    @Column(name = "title")
    private String title;

    @Column(name = "option_value")
    private String optionValue;

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
}
