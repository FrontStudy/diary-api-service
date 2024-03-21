package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.dtos.diaryShare.DiaryShareRequestDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.DiaryShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class DiaryShareController {

    private final DiaryShareService diaryShareService;

    @PatchMapping("/svc/diary/{diaryId}/shares")
    public ResponseDto updateDiaryShare(@PathVariable Long diaryId, @RequestBody DiaryShareRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(diaryShareService.updateDiaryShares(diaryId, userId, dto.getMemberIds()))
                .build();
    }

    @GetMapping("/svc/diary/{diaryId}/shares")
    public ResponseDto getMembersByDiary(@PathVariable Long diaryId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(diaryShareService.getMembersByDiaryId(userId, diaryId))
                .build();
    }

    @GetMapping("/svc/member/{memberId}/shares")
    public ResponseDto getSharedDiariesByMember(@PathVariable Long memberId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!memberId.equals(user.getUid())) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        return ResponseDto.builder()
                .status("success")
                .data(diaryShareService.getSharedDiariesByMemberId(memberId))
                .build();
    }


}
