package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.HashtagRequestDto;
import com.DateBuzz.Backend.controller.requestDto.PlaceImageRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordedPlaceRequestDto;
import com.DateBuzz.Backend.controller.responseDto.*;
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
import java.util.Objects;
import java.util.Optional;

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
        RecordEntity record = RecordEntity.FromRecordRequestDtoAndUserEntity(requestDto, user);
        recordRepository.saveAndFlush(record);
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

    public RecordResponseDto getrecord(Long recordId) {
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.DATE_NOT_FOUND, String.format("%s 에 해당하는 게시물이 존재하지 않습니다.", recordId)));
        List<RecordedPlaceEntity> places = recordedPlaceRepository.findAllByRecord(record);
        List<RecordedPlaceResponseDto> placeResponseDtos = new ArrayList<>();
        for(RecordedPlaceEntity place : places){
            List<PlaceImgResponseDto> imgResponseDtos = placeImgRepository
                    .findAllByRecordedPlace(place)
                    .stream()
                    .map(PlaceImgResponseDto::fromRecordedPlace)
                    .toList();
            placeResponseDtos.add(RecordedPlaceResponseDto.fromRecordedPlace(place, imgResponseDtos));
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

        return RecordResponseDto.fromRecord(record, placeResponseDtos, vibeTags, activityTags, customTags);
    }

    public Long deleteArticle(Long recordId, String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() ->new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", userName)));
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.DATE_NOT_FOUND, String.format("%s 에 해당하는 게시물이 존재하지 않습니다.", recordId)));
        if(Objects.equals(record.getUser().getId(), user.getId()))
            throw new DateBuzzException(ErrorCode.INVALID_USER, String.format("%s는 %d를 삭제할 권한이 없습니다.", userName, recordId));
        recordRepository.delete(record);
        return recordId;
    }
}
