package com.DateBuzz.Backend.controller.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequestDto {
    private String userName;
    private String title;
    private LocalDate dateDate;
    private String recordedContent;
    private List<HashtagRequestDto> hashtags;
    private List<RecordedPlaceRequestDto> recordedPlaces;
    private String exposure;

}
