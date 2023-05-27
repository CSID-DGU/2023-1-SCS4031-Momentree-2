package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.responseDto.HashtagResponseDto;
import com.DateBuzz.Backend.controller.responseDto.PlaceImgResponseDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.RecordedPlaceResponseDto;
import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.entity.HashtagType;
import com.DateBuzz.Backend.model.entity.PlaceImgEntity;
import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import com.DateBuzz.Backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final RecordRepository recordRepository;
    private final RecordedPlaceRepository recordedPlaceRepository;
    private final HashtagRepository hashtagRepository;
    private final PlaceImgRepository placeImgRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    public List<RecordResponseDto> getRecommendation(String rawList) {
        String raw = rawList.substring(1, rawList.length() - 1);
        String [] stringToList = raw.split(", ");

        List<Long> numbers = new ArrayList<>();
        for (String s : stringToList) {
            numbers.add(Long.parseLong(s));
        }
        List<RecordResponseDto> recordedList = new ArrayList<>();
        List<RecordEntity> records = new ArrayList<>();
        for(Long id : numbers){
             records.add(recordRepository.findById(id).orElseThrow(() -> new DateBuzzException(ErrorCode.DATE_NOT_FOUND, String.format("추천하고자 하는 %d 기록이 존재하지 않습니다.", id))));
        }
        for (RecordEntity record : records) {
            List<RecordedPlaceResponseDto> places = new ArrayList<>();
            List<RecordedPlaceEntity> recordedPlaces = recordedPlaceRepository.findAllByRecord(record);
            for(RecordedPlaceEntity place: recordedPlaces){
                List<PlaceImgEntity> imgEntities = placeImgRepository.findAllByRecordedPlace(place);
                List<PlaceImgResponseDto> imgResponseDtos = imgEntities
                        .stream()
                        .map(PlaceImgResponseDto::fromRecordedPlace)
                        .toList();
                RecordedPlaceResponseDto placeResponseDto = RecordedPlaceResponseDto.fromRecordedPlace(place, imgResponseDtos);
                places.add(placeResponseDto);
            }
            List<HashtagResponseDto> vibeTags = hashtagRepository
                    .findAllByRecordAndHashtagType(record, HashtagType.VIBE)
                    .stream()
                    .map(HashtagResponseDto::fromHashtag)
                    .toList();

            List<HashtagResponseDto> activityTags= hashtagRepository
                    .findAllByRecordAndHashtagType(record, HashtagType.ACTIVITY)
                    .stream()
                    .map(HashtagResponseDto::fromHashtag)
                    .toList();
            List<HashtagResponseDto> customTags= hashtagRepository
                    .findAllByRecordAndHashtagType(record, HashtagType.CUSTOM)
                    .stream()
                    .map(HashtagResponseDto::fromHashtag)
                    .toList();
            // like Cnt
            int likeCnt = likeRepository.countByRecord(record);

            // bookmark Cnt
            int bookmarkCnt = bookmarkRepository.countByRecord(record);

            RecordResponseDto recordResponseDto = RecordResponseDto.fromRecordNotLogin(record, places, vibeTags, activityTags, customTags, likeCnt, bookmarkCnt);
            recordedList.add(recordResponseDto);
        }
        return recordedList;
    }
}
