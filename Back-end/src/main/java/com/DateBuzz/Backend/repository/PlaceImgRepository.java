package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.PlaceImgEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceImgRepository extends JpaRepository<PlaceImgEntity, Long> {
    @Query(value = "select entity from PlaceImgEntity entity where entity.recordedPlace = :place order by entity.orders asc ")
    List<PlaceImgEntity> findAllByRecordedPlace(RecordedPlaceEntity place);
}
