package com.yuk.wazzangstudyrestapi1.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bookmark", uniqueConstraints = @UniqueConstraint(columnNames = {"diaryId", "memberId"}))
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long diaryId;

    @Column(nullable = false)
    private Long memberId;

    public Bookmark(Long diaryId, Long memberId) {
        this.diaryId = diaryId;
        this.memberId = memberId;
    }
}
