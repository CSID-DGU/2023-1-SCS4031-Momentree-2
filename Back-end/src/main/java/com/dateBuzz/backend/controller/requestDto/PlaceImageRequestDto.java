package com.dateBuzz.backend.controller.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceImageRequestDto {

    private Long placeId;
    private int orders;
    private byte[] imgFormData;
    private String imgUrl;
    private String fileName;
    private String contentType;
}
