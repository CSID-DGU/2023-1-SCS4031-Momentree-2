package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.UserJoinRequestDto;
import com.DateBuzz.Backend.controller.requestDto.UserLoginRequestDto;
import com.DateBuzz.Backend.controller.responseDto.UserJoinResponseDto;
import com.DateBuzz.Backend.controller.responseDto.UserLoginResponseDto;
import com.DateBuzz.Backend.exception.DateBuzzException;
import com.DateBuzz.Backend.exception.ErrorCode;
import com.DateBuzz.Backend.model.User;
import com.DateBuzz.Backend.model.entity.UserEntity;
import com.DateBuzz.Backend.repository.UserRepository;
import com.DateBuzz.Backend.util.JwtTokenUtils;
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

    public String login(UserLoginRequestDto requestDto) {
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
        return JwtTokenUtils.generateToken(user.getUserName(), user.getNickname(), secretKey, expiredTimeMs);
    }
}
