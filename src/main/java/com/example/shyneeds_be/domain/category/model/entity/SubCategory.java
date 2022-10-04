package com.example.shyneeds_be.domain.category.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Table(name = "sub_category")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "eng_title")
    private String engTitle;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "third_category_ids")
    private String thirdCategoryIds;

    @Column(name = "disp_flg")
    private boolean dispFlg;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
