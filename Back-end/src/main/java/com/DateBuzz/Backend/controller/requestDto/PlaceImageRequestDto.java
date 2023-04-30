package com.DateBuzz.Backend.controller.requestDto;

import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceImageRequestDto {

    private RecordedPlaceEntity place;
    private int orders;
    private String imgUrl;
}
