package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordedPlaceRepository extends JpaRepository<RecordedPlaceEntity, Long> {

    List<RecordedPlaceEntity> findAllByRecord(RecordEntity record);
}
