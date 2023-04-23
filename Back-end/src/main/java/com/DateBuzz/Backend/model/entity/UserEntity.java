package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "USER")
@SQLDelete(sql = "update \"user\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    private String profileImg;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }


}
