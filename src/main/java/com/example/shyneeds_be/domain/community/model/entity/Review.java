package com.example.shyneeds_be.domain.community.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Table(name = "Review")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "contents")
    private String contents;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "lookup_count")
    private int lookupCount;

    @Column(name = "disp_flg")
    private boolean dispFlg;

    @Column(name = "deleted_flg")
    private boolean deletedFlg;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // 조회수 증가
    public Review increaseLookup(){
        this.lookupCount++;

        return this;
    }

    // 수정
    public Review update(String title, String mainImage, String contents){
        this.title = title;
        this.mainImage = mainImage;
        this.contents = contents;
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());

        return this;
    }

    // 삭제 처리
    public Review delete(){
        this.deletedFlg = true;
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());

        return this;
    }


}
