package com.yuk.wazzangstudyrestapi1.dtos.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yuk.wazzangstudyrestapi1.domains.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiaryResponseDto {

    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long imgid;
    private String accessLevel;
    private Boolean active;


    public static DiaryResponseDto from(Diary diary) {
        return new DiaryResponseDto(
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
