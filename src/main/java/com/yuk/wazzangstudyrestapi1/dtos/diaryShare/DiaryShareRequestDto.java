package com.yuk.wazzangstudyrestapi1.dtos.diaryShare;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiaryShareRequestDto {
    List<Long> memberIds;
}
