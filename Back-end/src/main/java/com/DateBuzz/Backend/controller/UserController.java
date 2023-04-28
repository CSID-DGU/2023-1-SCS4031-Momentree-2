package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.UserJoinRequestDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.controller.responseDto.UserJoinResponseDto;
import com.DateBuzz.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
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
}
