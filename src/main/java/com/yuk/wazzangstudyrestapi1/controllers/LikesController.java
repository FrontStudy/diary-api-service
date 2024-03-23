package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/svc/diary/{diaryId}/likes")
    public ResponseDto addLikes(@PathVariable Long diaryId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(likesService.save(diaryId, userId))
                .build();
    }

    @DeleteMapping("/svc/diary/{diaryId}/likes")
    public ResponseDto deleteLikes(@PathVariable Long diaryId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(likesService.delete(diaryId, userId))
                .build();
    }
}
