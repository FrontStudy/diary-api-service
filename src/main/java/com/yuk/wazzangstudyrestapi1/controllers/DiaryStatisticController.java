package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.services.DiaryStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class DiaryStatisticController {

    private final DiaryStatisticService diaryStatisticService;

    @PostMapping("/pub/diaryStatistic/{diaryId}")
    public ResponseDto createOrIncreaseStatistic(@PathVariable Long diaryId) {
        return ResponseDto.builder()
                .status("success")
                .data(diaryStatisticService.increaseCount(diaryId))
                .build();
    }
}
