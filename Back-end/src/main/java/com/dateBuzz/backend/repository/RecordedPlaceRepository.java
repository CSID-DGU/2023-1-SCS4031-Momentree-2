package com.dateBuzz.backend.repository;

import com.dateBuzz.backend.model.entity.RecordEntity;
import com.dateBuzz.backend.model.entity.RecordedPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordedPlaceRepository extends JpaRepository<RecordedPlaceEntity, Long> {

    @Query(value = "select entity from RecordedPlaceEntity entity where entity.record = :record order by entity.orders asc ")
    List<RecordedPlaceEntity> findAllByRecord(@Param("record") RecordEntity record);

    Optional<RecordedPlaceEntity> findByIdAndRecord(Long placeId, RecordEntity record);
}