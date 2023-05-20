package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.entity.LikeEntity;
import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.UserEntity;
import com.DateBuzz.Backend.repository.LikeRepository;
import com.DateBuzz.Backend.repository.RecordRepository;
import com.DateBuzz.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;

    private final RecordRepository recordRepository;

    private final LikeRepository likeRepository;


    public void like(Long recordId, String name) {
        UserEntity user = userRepository.findByUserName(name)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", name)));
        RecordEntity record = recordRepository
                .findById(recordId)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.DATE_NOT_FOUND, String.format("%d 번호를 가진 기록을 찾을 수 없습니다.", recordId)));
        Optional<LikeEntity> like = likeRepository.findByUserAndRecord(user, record);
        like.ifPresent(LikeEntity::updateLikeStatus);
        if (like.isEmpty())likeRepository.save(LikeEntity.likeRecord(user, record));
    }
}