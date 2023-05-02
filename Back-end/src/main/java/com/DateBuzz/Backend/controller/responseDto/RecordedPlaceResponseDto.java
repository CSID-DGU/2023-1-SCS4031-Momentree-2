package com.DateBuzz.Backend.controller.responseDto;

import com.DateBuzz.Backend.model.entity.PlaceImgEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecordedPlaceResponseDto {

    private Long placeId;
    private Long recordId;
    private int order;
    private String placeName;
    private List<PlaceImgResponseDto> placeImages;

    public static RecordedPlaceResponseDto fromRecordedPlace(RecordedPlaceEntity recordedPlace, List<PlaceImgResponseDto> images){
        return new RecordedPlaceResponseDto(
                recordedPlace.getId(),
                recordedPlace.getRecord().getId(),
                recordedPlace.getOrders(),
                recordedPlace.getPlaceName(),
                images
        );
    }
}
