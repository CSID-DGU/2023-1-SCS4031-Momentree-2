package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;

@Table(name = "\"follow\"")
@Entity
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private UserEntity follwingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private UserEntity follower_id;
}
