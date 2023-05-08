package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.HashtagEntity;
import com.DateBuzz.Backend.model.entity.HashtagType;
import com.DateBuzz.Backend.model.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
    List<HashtagEntity> findAllByRecordAndHashtagType(RecordEntity record, HashtagType hashtagType);
}
