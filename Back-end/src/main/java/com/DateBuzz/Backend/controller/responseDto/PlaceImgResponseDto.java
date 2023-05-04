package com.DateBuzz.Backend.controller.responseDto;

import com.DateBuzz.Backend.model.entity.PlaceImgEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PlaceImgResponseDto {

    private String imageUrl;
    private int orders;

    public static PlaceImgResponseDto fromRecordedPlace(PlaceImgEntity image){
        return new PlaceImgResponseDto(
                image.getImgUrl(),
                image.getOrders()
        );
    }
}
