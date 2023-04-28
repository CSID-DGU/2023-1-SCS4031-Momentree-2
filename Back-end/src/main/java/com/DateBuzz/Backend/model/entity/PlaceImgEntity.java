package com.DateBuzz.Backend.model.entity;

import jakarta.persistence.*;

@Table(name = "\"place_img\"")
@Entity
public class PlaceImgEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private RecordedPlaceEntity recordedPlace;

    @Column(name = "orders")
    private int orders;

    @Column(name = "img_url")
    private String imgUrl;
}
