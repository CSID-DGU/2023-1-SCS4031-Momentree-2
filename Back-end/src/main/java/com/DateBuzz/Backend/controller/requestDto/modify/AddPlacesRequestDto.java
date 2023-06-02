package com.DateBuzz.Backend.controller.requestDto.modify;

import com.DateBuzz.Backend.controller.requestDto.RecordedPlaceRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddPlacesRequestDto {
    List<RecordedPlaceRequestDto> newPlaces;
}
