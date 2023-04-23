package com.DateBuzz.Backend.controller.requestDto;

import com.DateBuzz.Backend.model.entity.RecordEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class RecordedPlaceRequestDto {

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


}
