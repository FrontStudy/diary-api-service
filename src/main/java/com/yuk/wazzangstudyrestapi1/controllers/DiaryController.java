package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.PageInfoDto;
import com.yuk.wazzangstudyrestapi1.dtos.diary.DiaryListRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.diary.DiaryRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.diary.DiaryUpdateRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @CrossOrigin
    @PostMapping("/svc/diary")
    public ResponseDto diarySave(@RequestBody DiaryRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.save(dto, user.getUid()))
                .build();
    }

    @CrossOrigin
    @PutMapping("/svc/diary/{diaryId}")
    public ResponseDto update(@PathVariable Long diaryId, @RequestBody DiaryUpdateRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(dto.getTitle() == null || dto.getTitle().isEmpty() || dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_TITLE_OR_CONTENT);
        }

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.update(diaryId, user.getUid(), dto))
                .build();
    }

    @CrossOrigin
    @PostMapping("/svc/diaryList")
    public ResponseDto getDiaryListByMemberId(@RequestBody DiaryListRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageInfoDto pageInfo = PageInfoDto.builder().build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getListByMemberId(user.getUid(), dto, pageInfo))
                .page(pageInfo)
                .build();
    }
}
