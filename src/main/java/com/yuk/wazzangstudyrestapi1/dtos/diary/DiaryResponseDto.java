package com.yuk.wazzangstudyrestapi1.dtos.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiaryResponseDto extends BaseTimeEntity {

    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private String imgUrl;
    private String accessLevel;
    private Boolean active;
    private Long readCount;

    public DiaryResponseDto(LocalDateTime createdDate, LocalDateTime modifiedDate, Long id, Long memberId, String title, String content, String imgUrl, String accessLevel, Boolean active) {
        super(createdDate, modifiedDate);
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    public static DiaryResponseDto from(Diary diary) {
        return new DiaryResponseDto(
                diary.getCreatedDate(),
                diary.getModifiedDate(),
                diary.getId(),
                diary.getMemberId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getImgUrl(),
                diary.getAccessLevel(),
                diary.getActive()
        );
    }
}
