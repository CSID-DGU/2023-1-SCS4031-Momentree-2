package com.DateBuzz.Backend.controller.responseDto;

import com.DateBuzz.Backend.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserInfoResponseDto {
    private String nickname;
    private int follower;
    private int following;
    private int recordCnt;
    private String profileImg;

    public static UserInfoResponseDto getUserInfo(UserEntity user, int follower, int following, int recordCnt) {
        return new UserInfoResponseDto(
                user.getNickname(),
                follower,
                following,
                recordCnt,
                "");
    }
}
