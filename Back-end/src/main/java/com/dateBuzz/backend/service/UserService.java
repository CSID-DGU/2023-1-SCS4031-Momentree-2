package com.dateBuzz.backend.service;

import com.dateBuzz.backend.controller.requestDto.UserJoinRequestDto;
import com.dateBuzz.backend.controller.requestDto.UserLoginRequestDto;
import com.dateBuzz.backend.controller.responseDto.UserInfoResponseDto;
import com.dateBuzz.backend.controller.responseDto.UserJoinResponseDto;
import com.dateBuzz.backend.controller.responseDto.UserLoginResponseDto;
import com.dateBuzz.backend.exception.DateBuzzException;
import com.dateBuzz.backend.exception.ErrorCode;
import com.dateBuzz.backend.model.User;
import com.dateBuzz.backend.model.entity.UserEntity;
import com.dateBuzz.backend.repository.FollowRepository;
import com.dateBuzz.backend.repository.RecordRepository;
import com.dateBuzz.backend.repository.UserRepository;
import com.dateBuzz.backend.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder; // encoder 추가
    private final RecordRepository recordRepository;
    private final FollowRepository followRepository;


    public User loadUserByUserName(String userName){
        return userRepository.findByUserName(userName).map(User::fromEntity)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userName)));
    }
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

    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        UserEntity user = userRepository
                .findByUserName(requestDto.getUserName())
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", requestDto.getUserName())));

        // 비밀번호 체크
//        if(!encoder.encode(password).equals(user.getPassword())){
        if(!encoder.matches(requestDto.getPassword(), user.getPassword())){
            // if(!user.getPassword().equals(password)){ 암호화 하기 이전 password
            throw new DateBuzzException(ErrorCode.INVALID_PASSWORD);
        }
        // 토큰 생성 과정
        return new UserLoginResponseDto(JwtTokenUtils.generateToken(user.getUserName(), user.getNickname(), secretKey, expiredTimeMs), user.getNickname());
    }

    public UserInfoResponseDto getInfo(String userName) {
        UserEntity user = userRepository
                .findByUserName(userName)
                .orElseThrow(() -> new DateBuzzException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", userName)));
        int recordCnt = recordRepository.recordCnt(user);
        int followerCnt = followRepository.countFollower(user);
        int followingCnt = followRepository.countFollowing(user);
        return UserInfoResponseDto.getUserInfo(user, followerCnt, followingCnt, recordCnt);

    }
}