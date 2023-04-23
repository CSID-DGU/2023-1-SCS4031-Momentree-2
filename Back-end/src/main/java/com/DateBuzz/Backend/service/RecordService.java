package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.HashtagRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import com.DateBuzz.Backend.controller.requestDto.RecordedPlaceRequestDto;
import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.RecordedPlaceResponseDto;
import com.DateBuzz.Backend.model.entity.HashtagEntity;
import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import com.DateBuzz.Backend.model.entity.UserEntity;
import com.DateBuzz.Backend.repository.HashtagRepository;
import com.DateBuzz.Backend.repository.RecordRepository;
import com.DateBuzz.Backend.repository.RecordedPlaceRepository;
import com.DateBuzz.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RecordedPlaceRepository recordedPlaceRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;


    public Page<RecordResponseDto> getList(Pageable pageable) {

        List<RecordResponseDto> recordedList = new ArrayList<>();
        List<RecordEntity> records = recordRepository.findAll();
        for (RecordEntity record : records) {
            List<RecordedPlaceResponseDto> recordedPlaces = recordedPlaceRepository.findAllByRecord(record)
                    .stream()
                    .map(RecordedPlaceResponseDto::fromRecordedPlace)
                    .toList();
            RecordResponseDto recordResponseDto = RecordResponseDto.fromRecord(record, recordedPlaces);
            recordedList.add(recordResponseDto);
        }

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recordedList.size());
        return new PageImpl<>(recordedList.subList(start, end), pageable, recordedList.size());
    }

    public void writes(RecordRequestDto requestDto) throws Exception {
        UserEntity user = userRepository
                .findByUserName(requestDto.getUserName())
                .orElseThrow(Exception::new);
        RecordEntity record = RecordEntity.FromRecordRequestDtoAndUserEntity(requestDto, user);
        recordRepository.saveAndFlush(record);
        for(HashtagRequestDto hashtags: requestDto.getHashtags()){
            HashtagEntity hashtag = HashtagEntity.FromRecordRequestDtoAndRecordEntity(hashtags, record);
            hashtagRepository.save(hashtag);
        }
        for(RecordedPlaceRequestDto recordedPlaces: requestDto.getRecordedPlaces()){
            RecordedPlaceEntity recordedPlace = RecordedPlaceEntity.FromRecordedRequestDtoAndRecordEntity(recordedPlaces, record);
            recordedPlaceRepository.save(recordedPlace);
        }
    }
}
