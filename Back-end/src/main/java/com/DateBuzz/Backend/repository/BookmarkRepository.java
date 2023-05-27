package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.BookmarkEntity;
import com.DateBuzz.Backend.model.entity.RecordEntity;
import com.DateBuzz.Backend.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByUserAndRecord(UserEntity user, RecordEntity record);
    @Query(value = "select count(*) from BookmarkEntity entity where entity.record = :record and entity.bookmarkStatus = 1")
    Integer countByRecord(@Param("record") RecordEntity record);
    List<BookmarkEntity> findAllByUser(UserEntity user);
}
