package com.DateBuzz.Backend.model.entity;

import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "\"record\"")
@SQLDelete(sql = "update \"record\" set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String title;
    private String recordedContent;
    private LocalDate dateDate;
    @Enumerated(EnumType.STRING)
    private Exposure exposure;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt(){
        this.createdAt = Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }


    public static RecordEntity FromRecordRequestDtoAndUserEntity(RecordRequestDto requestDto, UserEntity user){
        RecordEntity record = new RecordEntity();
        record.user = user;
        record.title = requestDto.getTitle();
        record.recordedContent = requestDto.getRecordContent();
        record.dateDate = requestDto.getDateDate();
        record.exposure = Exposure.returnExposure(requestDto.getExposure());
        return record;
    }
}
