package com.yuk.wazzangstudyrestapi1.dtos.diary;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryRequestDto {
    private String title;
    private String content;
    private Long imgid;
    private String accessLevel = "private";
}
