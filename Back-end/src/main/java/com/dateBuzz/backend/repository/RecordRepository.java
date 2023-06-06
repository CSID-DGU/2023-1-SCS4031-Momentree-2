package com.dateBuzz.backend.repository;

import com.dateBuzz.backend.model.entity.RecordEntity;
import com.dateBuzz.backend.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    List<RecordEntity> findAllByUser(UserEntity user);
    @Query(value = "select count(*) from RecordEntity record where record.user = :user and record.deletedAt is null")
    Integer recordCnt(UserEntity user);

    @Query(value = "select record from RecordEntity record where record.user = :user and record.id = :id and record.deletedAt is null")
    Optional<RecordEntity> findByUserAndId(UserEntity user, Long id);
}
