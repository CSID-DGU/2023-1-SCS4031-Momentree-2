package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"record\"")
@SQLDelete(sql = "update \"record\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;
    private String recordedContent;


    private Exposure exposure;

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
