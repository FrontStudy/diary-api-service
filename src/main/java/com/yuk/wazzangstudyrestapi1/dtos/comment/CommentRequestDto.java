package com.yuk.wazzangstudyrestapi1.dtos.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long diaryId;
    private String content;
}
