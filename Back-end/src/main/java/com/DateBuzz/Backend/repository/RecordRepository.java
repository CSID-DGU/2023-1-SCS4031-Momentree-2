package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    List<RecordEntity> findAllByUser(UserEntity user);
    @Query(value = "select count(*) from RecordEntity record where record.user = :user")
    Integer myRecordCnt(UserEntity user);
}
