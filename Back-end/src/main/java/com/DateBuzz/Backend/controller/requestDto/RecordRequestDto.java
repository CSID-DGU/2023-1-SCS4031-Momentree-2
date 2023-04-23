package com.DateBuzz.Backend.controller.requestDto;

import java.time.LocalDate;
import java.util.List;

public class RecordRequestDto {
    private String userName;
    private String title;
    private LocalDate dateDate;
    private String recordContent;
    private List<HashtagRequestDto> hashtags;
    private List<RecordedPlaceRequestDto> recordedPlaces;

}
