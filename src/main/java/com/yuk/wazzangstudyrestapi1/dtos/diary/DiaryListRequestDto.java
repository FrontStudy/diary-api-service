package com.yuk.wazzangstudyrestapi1.dtos.diary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiaryListRequestDto {
    private int offset;
    private int size;
    private String sort;
}
