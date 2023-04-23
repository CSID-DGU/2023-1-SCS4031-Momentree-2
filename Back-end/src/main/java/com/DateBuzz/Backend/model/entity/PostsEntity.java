package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "POSTS")
@SQLDelete(sql = "update \"posts\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class PostsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
