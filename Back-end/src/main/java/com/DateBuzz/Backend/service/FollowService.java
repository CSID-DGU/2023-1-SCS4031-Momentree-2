package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.FollowRequestDto;
import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.entity.FollowEntity;
import com.DateBuzz.Backend.model.entity.UserEntity;
import com.DateBuzz.Backend.repository.FollowRepository;
import com.DateBuzz.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;

    private final FollowRepository followRepository;
    public Void follow(FollowRequestDto followName, String name) {
        UserEntity user = userRepository.findByUserName(name)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", name)));
        UserEntity followingUser = userRepository.findByNickname(followName.getNickName())
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 없는 유저입니다.", name)));
        Optional<FollowEntity> follow = followRepository.findByFollowingAndFollower(followingUser, user);
        follow.ifPresent(FollowEntity::updateFollow);
        if (follow.isEmpty())followRepository.save(FollowEntity.following(followingUser, user));
        return null;
    }
}
