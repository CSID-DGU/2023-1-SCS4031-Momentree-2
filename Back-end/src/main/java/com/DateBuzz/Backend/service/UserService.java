package com.DateBuzz.Backend.service;

import com.DateBuzz.Backend.controller.requestDto.UserJoinRequestDto;
import com.DateBuzz.Backend.controller.responseDto.UserJoinResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserJoinResponseDto join(UserJoinRequestDto requestDto) {
        return new UserJoinResponseDto();
    }
}
