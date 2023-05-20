package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Table(name = "\"likes\"")
@Entity
@Getter
@NoArgsConstructor
public class LikeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    @Column(name = "like_status")
    private int likeStatus;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Builder
    public LikeEntity(UserEntity user, RecordEntity record) {
        this.user = user;
        this.record = record;
        this.likeStatus = 1;
    }

    public static LikeEntity likeRecord(UserEntity user, RecordEntity record){
        return new LikeEntity(user, record);
    }

    public static void updateLikeStatus(LikeEntity like){
        if(like.likeStatus == 1){
            like.likeStatus = 0;
            return;
        }
        if(like.likeStatus == 0) like.likeStatus = 1;
    }

    @PrePersist
    void registeredAt(){
        this.createdAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
