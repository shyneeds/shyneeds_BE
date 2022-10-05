package com.example.shyneeds_be.domain.travel_package.model.entitiy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Table(name = "travel_package")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "required_option_name")
    private String requiredOptionName;

    @Column(name = "required_option_values")
    private String requiredOptionValues;

    @Column(name = "optional_name")
    private String optionalName;

    @Column(name = "optional_values")
    private String optionalValues;

    @Column(name = "flight_info")
    private String flightInfo;

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

}
