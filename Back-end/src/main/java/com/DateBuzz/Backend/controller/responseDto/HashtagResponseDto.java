package com.DateBuzz.Backend.controller.responseDto;

import com.DateBuzz.Backend.model.entity.HashtagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HashtagResponseDto {

    private String tagName;

    public static HashtagResponseDto fromHashtag(HashtagEntity hashtagEntity) {
        return new HashtagResponseDto(hashtagEntity.getTagName());
    }
}
