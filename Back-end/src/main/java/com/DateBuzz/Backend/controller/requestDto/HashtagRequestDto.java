package com.DateBuzz.Backend.controller.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class HashtagRequestDto {

    private String tagName;
    private String type;
    private Long recordId;

}
