package com.DateBuzz.Backend.controller.responseDto;

import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecordResponseDto {
    private static final String datePattern = "yyyy-MM-dd HH:mm:ss.SSS";
    private Long recordedId;
    private String UserName;
    private int followerCnt;
    private int followingCnt;
    private String profileImg;
    private String title;
    @JsonFormat(pattern = datePattern)
    private Timestamp createdAt;

    // TODO: 미개발로 인해 default 값 부여, 이후 개발 시 추가 예정
    private boolean bookMarkStatus;
    private boolean likeStatus;
    private int likeCnt;
    private int bookMarkCnt;

    private List<RecordedPlaceResponseDto> recordedPlaces;
    private List<HashtagResponseDto> VibeTags;
    private List<HashtagResponseDto> activityTags;
    private List<HashtagResponseDto> customTags;

    public static RecordResponseDto fromRecord(RecordEntity record, List<RecordedPlaceResponseDto> recordedPlaces, List<HashtagResponseDto> vibeTags, List<HashtagResponseDto> activityTags, List<HashtagResponseDto> customTags){
        return new RecordResponseDto(
                record.getId(),
                record.getUser().getUserName(),
                119,
                33,
                record.getUser().getProfileImg(),
                record.getTitle(),
                record.getCreatedAt(),
                true,
                true,
                134,
                23,
                recordedPlaces,
                vibeTags,
                activityTags,
                customTags
        );
    }

}
