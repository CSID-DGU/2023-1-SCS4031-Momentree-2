package com.DateBuzz.Backend.controller.responseDto;

import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecordedPlaceResponseDto {

    private Long placeId;
    private Long recordId;
    private int order;
    private String placeName;

    public static RecordedPlaceResponseDto fromRecordedPlace(RecordedPlaceEntity recordedPlace){
        return new RecordedPlaceResponseDto(
                recordedPlace.getId(),
                recordedPlace.getRecord().getId(),
                recordedPlace.getOrder(),
                recordedPlace.getPlaceName()
        );
    }
}
