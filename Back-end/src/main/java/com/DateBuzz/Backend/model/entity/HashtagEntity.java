package com.DateBuzz.Backend.model.entity;

import com.DateBuzz.Backend.controller.requestDto.HashtagRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "\"Hashtag\"")
@Getter
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

    public static HashtagEntity FromRecordRequestDtoAndRecordEntity(HashtagRequestDto requestDto, RecordEntity record){
        HashtagEntity hashtag = new HashtagEntity();
        hashtag.record = record;
        hashtag.hashtagType = HashtagType.returnHashtag(requestDto.getType());
        hashtag.tagName = requestDto.getTagName();
        return hashtag;
    }
}
