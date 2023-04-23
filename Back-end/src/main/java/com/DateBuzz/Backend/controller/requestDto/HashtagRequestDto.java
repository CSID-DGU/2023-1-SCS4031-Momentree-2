package com.DateBuzz.Backend.controller.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HashtagRequestDto {

    private String tagName;
    private String type;
    private Long recordId;

}
