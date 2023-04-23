package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Hashtag\"")
public class HashtagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tagName;

    @Enumerated(EnumType.STRING)
    private HashtagType hashtagType;

}
