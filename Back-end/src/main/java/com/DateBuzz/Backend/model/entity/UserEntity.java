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


}
