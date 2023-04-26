package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.UserJoinRequestDto;
import com.DateBuzz.Backend.controller.responseDto.UserJoinResponseDto;
import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.entity.UserEntity;
import com.DateBuzz.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder; // encoder 추가


    public UserJoinResponseDto join(UserJoinRequestDto requestDto) {
        //Todo: id 중복 체크
        userRepository.findByUserName(requestDto.getUserName())
                .ifPresent(it -> {
                    throw new DateBuzzException(ErrorCode.DUPLICATED_NAME, String.format("%s 는 이미 존재하는 아이디입니다.", requestDto.getUserName()));
                });
        //Todo: nickName 중복 체크
        userRepository.findByNickname(requestDto.getNickname())
                .ifPresent(it -> {
                    throw new DateBuzzException(ErrorCode.DUPLICATED_NAME, String.format("%s 는 이미 존재하는 닉네임입니다.", requestDto.getUserName()));
                });
        //Todo: 아이디 비밀번호 정규식 체크

        UserEntity user = UserEntity.fromJoinRequestDto(
                requestDto.getUserName(),
                encoder.encode(requestDto.getPassword()),
                requestDto.getNickname(),
                requestDto.getEmail());
        return UserJoinResponseDto.fromUser(userRepository.save(user));
    }
}
