package com.yuk.wazzangstudyrestapi1.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "diary_share")
public class DiaryShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long diaryId;

    @Column(nullable = false)
    private Long memberId;

    public DiaryShare(Long diaryId, Long memberId) {
        this.diaryId = diaryId;
        this.memberId = memberId;
    }
}