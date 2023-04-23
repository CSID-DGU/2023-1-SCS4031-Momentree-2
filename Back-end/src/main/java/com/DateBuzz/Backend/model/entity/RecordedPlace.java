package com.DateBuzz.Backend.model.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "recorded_place")
@SQLDelete(sql = "update \"recorded_place\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class RecordedPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    private int order;
    private String placeName;
    private String addressOld;
    private String addressRoad;
    private String addressGu;
    private String addressX;
    private String addressY;
    private String image;
    private String placeContent;
    private Timestamp createdAt;


    private Timestamp updatedAt;

    private Timestamp deletedAt;
    @PrePersist
    void registeredAt(){
        this.createdAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
