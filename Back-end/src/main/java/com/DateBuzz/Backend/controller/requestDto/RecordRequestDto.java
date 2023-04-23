package com.DateBuzz.Backend.controller.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecordRequestDto {
    private String userName;
    private String title;
    private LocalDate dateDate;
    private String recordContent;
    private List<HashtagRequestDto> hashtags;
    private List<RecordedPlaceRequestDto> recordedPlaces;

}
