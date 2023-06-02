package com.DateBuzz.Backend.controller.requestDto.modify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyRecordedPlaceInfoRequestDto {

    private Long placeId;
    private String newPlaceContent;
}
