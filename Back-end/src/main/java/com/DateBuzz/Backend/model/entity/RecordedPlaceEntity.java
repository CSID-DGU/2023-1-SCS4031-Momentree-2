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
@SQLDelete(sql = "update recorded_place set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
@Getter
public class RecordedPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    @Column(name = "orders")
    private int orders;
    @Column(name = "place_name")
    private String placeName;
    @Column(name = "address")
    private String address;
    @Column(name = "address_gu")
    private String addressGu;
    @Column(name = "address_x")
    private String addressX;
    @Column(name = "address_y")
    private String addressY;
    @Column(name = "place_content")
    private String placeContent;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public static RecordedPlaceEntity FromRecordedRequestDtoAndRecordEntity(RecordedPlaceRequestDto recordedPlaceDto, RecordEntity record) {
        RecordedPlaceEntity recordedPlace = new RecordedPlaceEntity();
        recordedPlace.record = record;
        recordedPlace.orders = recordedPlaceDto.getOrders();
        recordedPlace.placeName = recordedPlaceDto.getPlaceName();
        recordedPlace.address = recordedPlaceDto.getAddress();
        recordedPlace.addressGu = recordedPlaceDto.getAddressGu();
        recordedPlace.addressX = recordedPlaceDto.getAddressX();
        recordedPlace.addressY = recordedPlaceDto.getAddressY();
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
