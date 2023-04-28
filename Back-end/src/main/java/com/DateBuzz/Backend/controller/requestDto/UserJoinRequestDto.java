package com.DateBuzz.Backend.controller.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequestDto {
    private String userName;
    private String password;
    private String nickname;
    private String email;

}
