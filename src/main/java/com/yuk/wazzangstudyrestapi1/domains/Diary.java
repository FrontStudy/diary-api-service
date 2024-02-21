package com.yuk.wazzangstudyrestapi1.domains;

import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String content;

    private Long imgid;

    @Column(nullable = false)
    @Builder.Default
    private String accessLevel = "private";

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Builder
    public Diary(Long id, Long memberId, String title, String content, Long imgid, String accessLevel, boolean active) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imgid = imgid;
        this.accessLevel = accessLevel;
        this.active = active;
    }

}
