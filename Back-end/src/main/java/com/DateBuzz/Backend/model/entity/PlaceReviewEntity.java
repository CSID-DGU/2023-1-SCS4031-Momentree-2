package com.DateBuzz.Backend.model.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "PLACE_REVIEW")
@SQLDelete(sql = "update \"place_review\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class PlaceReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
