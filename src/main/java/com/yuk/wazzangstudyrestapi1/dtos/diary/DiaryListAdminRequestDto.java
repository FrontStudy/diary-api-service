package com.yuk.wazzangstudyrestapi1.dtos.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiaryListAdminRequestDto {
    private int offset;
    private int size;
    private String title;
    private String accessLevel;
    private Boolean active;
    private Long memberId;
}
