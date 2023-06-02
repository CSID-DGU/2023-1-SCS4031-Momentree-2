package com.dateBuzz.backend.repository;

import com.dateBuzz.backend.model.entity.FollowEntity;
import com.dateBuzz.backend.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByFollowingAndFollower(UserEntity following, UserEntity follower);

    @Query(value = "select count(*) from FollowEntity entity where entity.following = :user and entity.followStatus = 1")
    Integer countFollower(@Param("user") UserEntity user);

    @Query(value = "select count(*) from FollowEntity entity where entity.follower = :user and entity.followStatus = 1")
    Integer countFollowing(@Param("user") UserEntity user);
}
