package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.controller.requestDto.FollowRequestDto;
import com.DateBuzz.Backend.controller.responseDto.Response;
import com.DateBuzz.Backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public Response<Void> follow(FollowRequestDto requestDto, Authentication authentication){
        return Response.success(followService.follow(requestDto, authentication.getName()));
    }

}
