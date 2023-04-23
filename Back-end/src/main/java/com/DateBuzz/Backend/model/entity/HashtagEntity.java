package com.DateBuzz.Backend.model.entity;

import com.DateBuzz.Backend.controller.requestDto.HashtagRequestDto;
import jakarta.persistence.*;

@Entity
@Table(name = "\"Hashtag\"")
public class HashtagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    private String tagName;

    @Enumerated(EnumType.STRING)
    private HashtagType hashtagType;


}
