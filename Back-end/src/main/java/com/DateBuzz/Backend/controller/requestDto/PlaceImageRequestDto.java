package com.DateBuzz.Backend.controller.requestDto;

import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceImageRequestDto {

    private Long placeId;
    private int orders;
    private String imgUrl;
}
