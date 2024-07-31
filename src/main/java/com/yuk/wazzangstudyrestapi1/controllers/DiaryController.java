package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.PageInfoDto;
import com.yuk.wazzangstudyrestapi1.dtos.diary.DiaryListAdminRequestDto;
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

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/svc/diary")
    public ResponseDto diarySave(@RequestBody DiaryRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.save(dto, user.getUid()))
                .build();
    }

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

    @GetMapping("svc/diaryDetail/{diaryId}")
    public ResponseDto getDiaryDetailById(@PathVariable Long diaryId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getAccessibleDiaryDetailById(diaryId, user.getUid()))
                .build();
    }

    @GetMapping("/svc/me/diaryList")
    public ResponseDto getDiaryListByMemberId(@RequestParam int offset, @RequestParam int size) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getListByMemberId(user.getUid(), dto, pageInfo))
                .page(pageInfo)
                .build();
    }

    @GetMapping("/svc/shared/diaryList")
    public ResponseDto getSharedDiaryList(@RequestParam int offset, @RequestParam int size) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getSharedDiaryListByMemberId(user.getUid(), dto, pageInfo))
                .page(pageInfo)
                .build();
    }

    @GetMapping("/svc/shared/diaryDetailList")
    public ResponseDto getSharedDiaryDetailList(@RequestParam int offset, @RequestParam int size) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getSharedDiaryDetailListByMemberId(user.getUid(), dto, pageInfo))
                .page(pageInfo)
                .build();
    }

    @GetMapping("/pub/diaryList")
    public ResponseDto getPublicDiaryList(@RequestParam int offset, @RequestParam int size) {
        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getpublicDiaryList(dto, pageInfo))
                .page(pageInfo)
                .build();
    }

    @GetMapping("/svc/pubDiaryDetailList")
    public ResponseDto getPublicDiaryDetailList(@RequestParam int offset, @RequestParam int size, @RequestParam(required = false, defaultValue = "latest") String sort) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .sort(sort)
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getpublicDiaryDetailList(dto, pageInfo, userId))
                .page(pageInfo)
                .build();
    }

    @PostMapping("/admin/diaryList")
    public ResponseDto getDiaryListAsAdmin(@RequestBody DiaryListAdminRequestDto dto) {
        PageInfoDto pageInfo = PageInfoDto.builder().build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getListAsAdmin(dto, pageInfo))
                .page(pageInfo)
                .build();
    }

    @PostMapping("/admin/diaryDetailList")
    public ResponseDto getDiaryDetailListAsAdmin(@RequestBody DiaryListAdminRequestDto dto) {
        PageInfoDto pageInfo = PageInfoDto.builder().build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getDetailListAsAdmin(dto, pageInfo))
                .page(pageInfo)
                .build();
    }


    @GetMapping("svc/member/me/bookmark/diary")
    public ResponseDto getBookmarkDiaries(@RequestParam int offset, @RequestParam int size) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getBookmarkedDiaryList(userId, dto, pageInfo))
                .build();

    }

    @GetMapping("svc/member/me/popular/diary")
    public ResponseDto getMyPopularDiaries(@RequestParam int offset, @RequestParam int size) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        PageInfoDto pageInfo = PageInfoDto.builder().build();

        DiaryListRequestDto dto = DiaryListRequestDto.builder()
                .offset(offset)
                .size(size)
                .sort("likes")
                .build();

        return ResponseDto.builder()
                .status("success")
                .data(diaryService.getMyDiaryList(dto, pageInfo, userId))
                .build();

    }
}
