package com.dateBuzz.backend.controller;

import com.dateBuzz.backend.controller.requestDto.UserJoinRequestDto;
import com.dateBuzz.backend.controller.requestDto.UserLoginRequestDto;
import com.dateBuzz.backend.controller.responseDto.Response;
import com.dateBuzz.backend.controller.responseDto.UserInfoResponseDto;
import com.dateBuzz.backend.controller.responseDto.UserJoinResponseDto;
import com.dateBuzz.backend.controller.responseDto.UserLoginResponseDto;
import com.dateBuzz.backend.service.UserService;
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
        return Response.success(userService.login(requestDto));
    }

    @GetMapping("/userInfo")
    public Response<UserInfoResponseDto> getUserInfo(Authentication authentication){
        return Response.success(userService.getInfo(authentication.getName()));
    }
}
