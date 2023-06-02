package com.dateBuzz.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "\"follow\"")
@Entity
@Getter
@NoArgsConstructor
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private UserEntity following;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @Column(name = "follow_status")
    private int followStatus;

    private FollowEntity(UserEntity following, UserEntity follower) {
        this.following = following;
        this.follower = follower;
        this.followStatus = 1;
    }

    public static FollowEntity following(UserEntity following, UserEntity follower){
        return new FollowEntity(following, follower);
    }

    public static void updateFollow(FollowEntity follow){
        if(follow.getFollowStatus() == 0) {
            follow.followStatus = 1;
            return;
        }
        if(follow.getFollowStatus() == 1){
            follow.followStatus = 0;
        }
    }
}
