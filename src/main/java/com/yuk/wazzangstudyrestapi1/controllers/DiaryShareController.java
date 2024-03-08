package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.DiaryShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryShareController {

    private final DiaryShareService diaryShareService;

    @CrossOrigin
    @PostMapping("/svc/diary/{diaryId}/shares")
    public ResponseDto addDiaryShare(@PathVariable Long diaryId, @RequestBody List<Long> memberIds) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(diaryShareService.addDiaryShares(diaryId, userId, memberIds))
                .build();
    }


}
