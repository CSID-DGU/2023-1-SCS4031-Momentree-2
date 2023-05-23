package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.UserJoinRequestDto;
import com.DateBuzz.Backend.controller.requestDto.UserLoginRequestDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.controller.responseDto.UserInfoResponseDto;
import com.DateBuzz.Backend.controller.responseDto.UserJoinResponseDto;
import com.DateBuzz.Backend.controller.responseDto.UserLoginResponseDto;
import com.DateBuzz.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponseDto> join(@RequestBody UserJoinRequestDto requestDto){
        UserJoinResponseDto responseDto = userService.join(requestDto);
        return Response.success(responseDto);
    }

    @PostMapping("/login")
    public Response<UserLoginResponseDto> join(@RequestBody UserLoginRequestDto requestDto){
        String token = userService.login(requestDto);
        return Response.success(new UserLoginResponseDto(token));
    }

    @GetMapping("/UserInfo")
    public Response<UserInfoResponseDto> getUserInfo(Authentication authentication){
        return Response.success(userService.getInfo(authentication.getName()));
    }
}
