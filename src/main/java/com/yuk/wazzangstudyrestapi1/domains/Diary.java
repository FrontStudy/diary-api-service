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

    private String imgUrl;

    @Column(nullable = false)
    @Builder.Default
    private String accessLevel = "private";

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Builder
    public Diary(Long id, Long memberId, String title, String content, String imgUrl, String accessLevel, Boolean active) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    public void update(String title, String content, String imgUrl, String accessLevel, Boolean active) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }

}
