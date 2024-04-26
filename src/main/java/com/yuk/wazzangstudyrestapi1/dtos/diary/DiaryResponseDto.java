package com.yuk.wazzangstudyrestapi1.dtos.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private Long imgid;
    private String accessLevel;
    private Boolean active;
    private Long readCount;

    public DiaryResponseDto(LocalDateTime createdDate, LocalDateTime modifiedDate, Long id, Long memberId, String title, String content, Long imgid, String accessLevel, Boolean active) {
        super(createdDate, modifiedDate);
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.imgid = imgid;
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
                diary.getImgid(),
                diary.getAccessLevel(),
                diary.getActive()
        );
    }
}
