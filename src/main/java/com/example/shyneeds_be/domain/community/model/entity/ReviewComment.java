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

@Table(name = "review_comment")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "deleted_flg")
    private boolean deletedFlg;

    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // 댓글 업데이트
    public ReviewComment update(String comment){
        this.comment = comment;
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
        return this;
    }

    // 삭제
    public ReviewComment delete(){
        this.deletedFlg = true;
        return this;
    }
}
