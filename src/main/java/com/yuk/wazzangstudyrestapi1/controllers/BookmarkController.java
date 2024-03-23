package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/svc/diary/{diaryId}/bookmark")
    public ResponseDto addBookmark(@PathVariable Long diaryId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(bookmarkService.save(diaryId, userId))
                .build();
    }

    @DeleteMapping("/svc/diary/{diaryId}/bookmark")
    public ResponseDto deleteLikes(@PathVariable Long diaryId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(bookmarkService.delete(diaryId, userId))
                .build();
    }
}
