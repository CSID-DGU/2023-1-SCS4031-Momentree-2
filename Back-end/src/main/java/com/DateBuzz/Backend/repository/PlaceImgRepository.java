package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.PlaceImgEntity;
import com.DateBuzz.Backend.model.entity.RecordedPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceImgRepository extends JpaRepository<PlaceImgEntity, Long> {
    List<PlaceImgEntity> findAllByRecordedPlace(RecordedPlaceEntity place);
}
