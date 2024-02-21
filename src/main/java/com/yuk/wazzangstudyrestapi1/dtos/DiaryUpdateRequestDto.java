package com.yuk.wazzangstudyrestapi1.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiaryUpdateRequestDto {
    private Long memberId;
    private String title;
    private String content;
    private Long imgid;
    private String accessLevel;
    private Boolean active;
}
