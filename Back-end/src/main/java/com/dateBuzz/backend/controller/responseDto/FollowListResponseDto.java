package com.dateBuzz.backend.controller.responseDto;

import com.dateBuzz.backend.model.entity.FollowEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowListResponseDto {
    private String userName;
    private int follower;
    private int following;
    private String imgUrl;

    public static FollowListResponseDto fromFollower(FollowEntity entity, int followerCnt, int followingCnt) {
        return new FollowListResponseDto(
                entity.getFollower().getUserName(),
                followerCnt,
                followingCnt,
                entity.getFollower().getProfileImg()
        );
    }
}
