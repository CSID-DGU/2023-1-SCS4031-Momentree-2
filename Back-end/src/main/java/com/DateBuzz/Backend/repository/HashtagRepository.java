package com.DateBuzz.Backend.repository;

import com.DateBuzz.Backend.model.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
}
