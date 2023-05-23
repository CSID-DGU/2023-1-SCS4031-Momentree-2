package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.*;
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
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final EntityManager entityManager;
    private final S3Service s3Service;

    // 로그인 안한 채로 리스트 받기
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
            // like Cnt
            int likeCnt = likeRepository.countByRecord(record);

            // bookmark Cnt
            int bookmarkCnt = bookmarkRepository.countByRecord(record);

            RecordResponseDto recordResponseDto = RecordResponseDto.fromRecordNotLogin(record, places, vibeTags, activityTags, customTags, likeCnt, bookmarkCnt);
            recordedList.add(recordResponseDto);
        }
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recordedList.size());
        return new PageImpl<>(recordedList.subList(start, end), pageable, recordedList.size());
    }

    // 로그인 한 채로 리스트 받기
    public Page<RecordResponseDto> getListLogin(Pageable pageable, String userName) {
        UserEntity user = userRepository
                .findByUserName(userName)
                .orElseThrow(() ->new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", userName)));
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
            // like Cnt
            int likeCnt = likeRepository.countByRecord(record);

            // like Status
            int likeStatus = 0;
            Optional<LikeEntity> like = likeRepository.findByUserAndRecord(user, record);
            if(like.isPresent() && like.get().getLikeStatus() == 1) likeStatus++;

            // bookmark Cnt
            int bookmarkCnt = bookmarkRepository.countByRecord(record);

            // bookmark Status
            int bookmarkStatus = 0;
            Optional<BookmarkEntity> bookmark = bookmarkRepository.findByUserAndRecord(user, record);
            if(bookmark.isPresent() && bookmark.get().getBookmarkStatus() == 1) bookmarkStatus++;

            RecordResponseDto recordResponseDto = RecordResponseDto.fromRecord(record, places, vibeTags, activityTags, customTags, likeStatus, likeCnt, bookmarkStatus, bookmarkCnt);
            recordedList.add(recordResponseDto);
        }

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recordedList.size());
        return new PageImpl<>(recordedList.subList(start, end), pageable, recordedList.size());
    }



    public RecordResponseDto getrecordLogin(Long recordId, String userName) {
        UserEntity user = userRepository
                .findByUserName(userName)
                .orElseThrow(() ->new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", userName)));
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

        // like Cnt
        int likeCnt = likeRepository.countByRecord(record);

        // like Status
        int likeStatus = 0;
        Optional<LikeEntity> like = likeRepository.findByUserAndRecord(user, record);
        if(like.isPresent() && like.get().getLikeStatus() == 1) likeStatus++;

        // bookmark Cnt
        int bookmarkCnt = bookmarkRepository.countByRecord(record);

        // bookmark Status
        int bookmarkStatus = 0;
        Optional<BookmarkEntity> bookmark = bookmarkRepository.findByUserAndRecord(user, record);
        if(bookmark.isPresent() && bookmark.get().getBookmarkStatus() == 1) bookmarkStatus++;

        return RecordResponseDto.fromRecord(record, placeResponseDtos, vibeTags, activityTags, customTags, likeStatus, likeCnt, bookmarkStatus, bookmarkCnt);
    }

    public Long deleteArticle(Long recordId, String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() ->new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", userName)));
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.DATE_NOT_FOUND, String.format("%s 에 해당하는 게시물이 존재하지 않습니다.", recordId)));
        if(record.getUser() != user)
            throw new DateBuzzException(ErrorCode.INVALID_USER, String.format("%s는 %d를 삭제할 권한이 없습니다.", userName, recordId));
        recordRepository.delete(record);
        return recordId;
    }

    public Page<RecordResponseDto> getMyRecord(Pageable pageable, String name) {
        UserEntity user = userRepository.findByUserName(name)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", name)));
        List<RecordEntity> records = recordRepository.findAllByUser(user);
        List<RecordResponseDto> recordedList = new ArrayList<>();
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

            // like Status
            int likeStatus = 0;
            if(likeRepository.findByUserAndRecord(user, record).isPresent()) likeStatus++;
            // bookmark Cnt
            int bookmarkCnt = bookmarkRepository.countByRecord(record);

            // bookmark Status
            int bookmarkStatus = 0;
            Optional<BookmarkEntity> bookmark = bookmarkRepository.findByUserAndRecord(user, record);
            if(bookmark.isPresent() && bookmark.get().getBookmarkStatus() == 1) bookmarkStatus++;

            RecordResponseDto recordResponseDto = RecordResponseDto.fromRecord(record, places, vibeTags, activityTags, customTags, likeStatus, likeCnt, bookmarkStatus, bookmarkCnt);
            recordedList.add(recordResponseDto);

        }
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recordedList.size());
        return new PageImpl<>(recordedList.subList(start, end), pageable, recordedList.size());
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

        // like Cnt
        int likeCnt = likeRepository.countByRecord(record);
        // bookmark Cnt
        int bookmarkCnt = bookmarkRepository.countByRecord(record);

        return RecordResponseDto.fromRecordNotLogin(record, placeResponseDtos, vibeTags, activityTags, customTags, likeCnt, bookmarkCnt);

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
                    String imageName = imageRequestDto.getFileName();
                    String contentType = imageRequestDto.getContentType();
                    String url = s3Service.uploadFile(imageRequestDto.getImgFormData(), imageName, contentType);
                    PlaceImgEntity placeImg = PlaceImgEntity.FromPlaceImgRequestDto(recordedPlace, imageRequestDto, url);
                    placeImgRepository.save(placeImg);
                }
            }
            recordedPlaceRepository.save(recordedPlace);
        }
    }

//    public void modifyRecord(Long recordId, ModifyRecordRequestDto requestDto, String userName) {
//        UserEntity user = userRepository
//                .findByUserName(userName)
//                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", userName)));
//
//        RecordEntity record = recordRepository
//                .findByUserAndId(user, recordId)
//                .orElseThrow(() -> new DateBuzzException(ErrorCode.DATE_NOT_FOUND, String.format("%s가 작성한 %d 기록이 없습니다.", userName, recordId)));
//
//        record.modifyRecord(requestDto);
//
//        List<HashtagEntity> hashtags = hashtagRepository.findAllByRecord(record);
//        List<String> hashtagNames = hashtags.stream().map(HashtagEntity::getTagName).toList();
//        List<String> changedNames = requestDto.getHashtags().stream().map(HashtagRequestDto::getTagName).toList();
//        for(int i = 0; i < hashtagNames.size(); i++){
//            if(!changedNames.contains(hashtagNames.get(i))) hashtagRepository.delete(hashtags.get(i));
//        }
//        for(int i = 0; i < changedNames.size(); i++){
//            if(!hashtagNames.contains(changedNames.get(i))) hashtagRepository.save(HashtagEntity.FromRecordRequestDtoAndRecordEntity(requestDto.getHashtags().get(i), record));
//        }
//
//        List<RecordedPlaceEntity> recordedPlaces = recordedPlaceRepository.findAllByRecord(record);
//        List<RecordedPlaceRequestDto> recordedPlaceDtos  = requestDto.getRecordedPlaces();
//        recordedPlaces.sort(Comparator.comparingLong(RecordedPlaceEntity::getId));
//        recordedPlaceDtos.sort(Comparator.comparingLong(recordedPlaceDtos.));
//
//        List<PlaceImgEntity> images = placeImgRepository.findAllByRecordedPlace(recordedPlaces);
//
//
//
//    }
}
