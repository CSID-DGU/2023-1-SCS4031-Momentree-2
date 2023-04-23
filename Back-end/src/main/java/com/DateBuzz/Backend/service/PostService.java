package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.responseDto.RecordResponseDto;
import com.DateBuzz.Backend.controller.responseDto.RecordedPlaceResponseDto;
import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import com.DateBuzz.Backend.repository.RecordRepository;
import com.DateBuzz.Backend.repository.RecordedPlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PostService {

    private RecordRepository recordRepository;
    private RecordedPlaceRepository recordedPlaceRepository;
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
}
