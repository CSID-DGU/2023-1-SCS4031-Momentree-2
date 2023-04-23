package com.DateBuzz.Backend.model.entity;


import com.DateBuzz.Backend.controller.requestDto.RecordedPlaceRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"recorded_place\"")
@SQLDelete(sql = "update \"recorded_place\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
@Getter
public class RecordedPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    private int orders;
    private String placeName;
    private String addressOld;
    private String addressRoad;
    private String addressGu;
    private String addressX;
    private String addressY;
    private String image;
    private String placeContent;
    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static RecordedPlaceEntity FromRecordedRequestDtoAndRecordEntity(RecordedPlaceRequestDto recordedPlaceDto, RecordEntity record) {
        RecordedPlaceEntity recordedPlace = new RecordedPlaceEntity();
        recordedPlace.record = record;
        recordedPlace.orders = recordedPlaceDto.getOrders();
        recordedPlace.placeName = recordedPlaceDto.getPlaceName();
        recordedPlace.addressOld = recordedPlaceDto.getAddressOld();
        recordedPlace.addressRoad = recordedPlaceDto.getAddressRoad();
        recordedPlace.addressGu = recordedPlaceDto.getAddressGu();
        recordedPlace.addressX = recordedPlaceDto.getAddressX();
        recordedPlace.addressY = recordedPlaceDto.getAddressY();
        recordedPlace.image = recordedPlaceDto.getImage();
        recordedPlace.placeContent = recordedPlaceDto.getPlaceContent();
        return recordedPlace;
    }

    @PrePersist
    void registeredAt(){
        this.createdAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
