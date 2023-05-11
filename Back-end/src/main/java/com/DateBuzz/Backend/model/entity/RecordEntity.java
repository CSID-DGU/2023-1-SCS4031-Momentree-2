package com.DateBuzz.Backend.model.entity;

import com.DateBuzz.Backend.controller.requestDto.RecordRequestDto;
import jakarta.persistence.*;
import lombok.*;
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
    @Column(name = "title")
    private String title;
    @Column(name = "recorded_content")
    private String recordedContent;
    @Column(name = "date_date")
    private String dateDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "exposure")
    private Exposure exposure;

    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "deleted_at")
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
        record.recordedContent = requestDto.getRecordedContent();
        record.dateDate = requestDto.getDateDate();
        record.exposure = Exposure.returnExposure(requestDto.getExposure());
        return record;
    }
}
