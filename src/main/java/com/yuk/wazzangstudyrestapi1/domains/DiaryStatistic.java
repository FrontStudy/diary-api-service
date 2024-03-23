package com.yuk.wazzangstudyrestapi1.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long diaryId;

    @Builder.Default
    private Long readCount = 1L;

    public void increaseReadCount() {
        this.readCount += 1;
    }
}