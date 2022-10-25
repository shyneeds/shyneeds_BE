package com.example.shyneeds_be.domain.travel_package.model.entitiy;

import com.example.shyneeds_be.domain.cart.model.entity.Cart;
import com.example.shyneeds_be.domain.reservation_package.model.entity.ReservationPackage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "travel_package")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "category_ids")
    private String categoryIds;

    @Column(name = "sub_category_ids")
    private String subCategoryIds;

    @Column(name = "third_category_ids")
    private String thirdCategoryIds;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "description_image")
    private String descriptionImage;

    @Column(name = "price")
    private String price; // 가격문의 옵션이 있으므로 String 처리

    @Column(name = "summary")
    private String summary;

    @OneToMany(mappedBy = "travelPackage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PackageOption> packageOptionList;

    @Column(name = "soldout_flg")
    private boolean soldoutFlg;

    @Column(name = "disp_flg")
    private boolean dispFlg;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "search_keyword")
    private String searchKeyword;

    @Column(name = "deleted_flg")
    private boolean deletedFlg;

    @Column(name = "main_banner_flg")
    private boolean mainBannerFlg;

    @JsonBackReference
    @OneToMany(mappedBy = "travelPackage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReservationPackage> reservationPackages = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "travelPackage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    // 상품 삭제
    public TravelPackage deleted(){
        this.deletedFlg = true;
        return this;
    }

}
