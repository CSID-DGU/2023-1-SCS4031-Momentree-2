package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.HashtagRequestDto;
import com.DateBuzz.Backend.controller.requestDto.PlaceImageRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordedPlaceRequestDto;
import com.DateBuzz.Backend.controller.responseDto.HashtagResponseDto;
import com.DateBuzz.Backend.controller.responseDto.PlaceImgResponseDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.RecordedPlaceResponseDto;
import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.entity.*;
import com.DateBuzz.Backend.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordedPlaceRepository recordedPlaceRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final PlaceImgRepository placeImgRepository;

    private final EntityManager entityManager;


    public Page<RecordResponseDto> getList(Pageable pageable) {

        List<RecordResponseDto> recordedList = new ArrayList<>();
        List<RecordEntity> records = recordRepository.findAll();
        List<RecordedPlaceResponseDto> places = new ArrayList<>();
        for (RecordEntity record : records) {
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
            String query = "select h from HashtagEntity as h where h.record = :record and h.tagName = :tagName";
            List<HashtagResponseDto> vibeTags = entityManager.createQuery(query, HashtagEntity.class)
                    .setParameter("record", record)
                    .setParameter("tagName", "VIBE")
                    .getResultList()
                    .stream().map(HashtagResponseDto::fromHashtag)
                    .toList();
            List<HashtagResponseDto> activityTags= entityManager.createQuery(query, HashtagEntity.class)
                    .setParameter("record", record)
                    .setParameter("tagName", "ACTIVITY")
                    .getResultList()
                    .stream().map(HashtagResponseDto::fromHashtag)
                    .toList();
            List<HashtagResponseDto> customTags= entityManager.createQuery(query, HashtagEntity.class)
                    .setParameter("record", record)
                    .setParameter("tagName", "CUSTOM")
                    .getResultList()
                    .stream().map(HashtagResponseDto::fromHashtag)
                    .toList();
            RecordResponseDto recordResponseDto = RecordResponseDto.fromRecord(record, places, vibeTags, activityTags, customTags);
            recordedList.add(recordResponseDto);
        }

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recordedList.size());
        return new PageImpl<>(recordedList.subList(start, end), pageable, recordedList.size());
    }

    public void writes(RecordRequestDto requestDto, String userName){
        UserEntity user = userRepository
                .findByUserName(userName)
                .orElseThrow(() ->new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", userName)));
        System.out.println(user.getUserName());
        RecordEntity record = RecordEntity.FromRecordRequestDtoAndUserEntity(requestDto, user);
        recordRepository.saveAndFlush(record);
        System.out.println(record.getId());
        for(HashtagRequestDto hashtags: requestDto.getHashtags()){
            HashtagEntity hashtag = HashtagEntity.FromRecordRequestDtoAndRecordEntity(hashtags, record);
            hashtagRepository.save(hashtag);
        }
        for(RecordedPlaceRequestDto recordedPlaces: requestDto.getRecordedPlaces()){
            RecordedPlaceEntity recordedPlace = RecordedPlaceEntity.FromRecordedRequestDtoAndRecordEntity(recordedPlaces, record);
            if(!(recordedPlaces.getImages() == null)){
                for(PlaceImageRequestDto imageRequestDto: recordedPlaces.getImages()){
                    PlaceImgEntity placeImg = PlaceImgEntity.FromPlaceImgRequestDto(recordedPlace, imageRequestDto);
                    placeImgRepository.save(placeImg);
                }
            }
            recordedPlaceRepository.save(recordedPlace);
        }
    }
}
