package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.PageInfoDto;
import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentResponseDto;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/svc/comment")
    public ResponseDto commentSave(@RequestBody CommentRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseDto.builder()
                .status("success")
                .data(commentService.save(dto, user.getUid()))
                .build();
    }

    @GetMapping("/pub/diary/{diaryId}/comment")
    public ResponseDto commentListByPubDiary(
            @PathVariable Long diaryId,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageInfoDto pageInfo = PageInfoDto.builder().build();
        Pageable pageable = PageRequest.of(offset, size, Sort.by("createdDate").descending());

        List<CommentResponseDto> comments = commentService.getCommentByPubDiary(diaryId, pageInfo, pageable);
        return ResponseDto.builder()
                .status("success")
                .data(comments)
                .build();
    }

    @GetMapping("/svc/diary/{diaryId}/comment")
    public ResponseDto commentListByDiary(
            @PathVariable Long diaryId,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageInfoDto pageInfo = PageInfoDto.builder().build();
        Pageable pageable = PageRequest.of(offset, size, Sort.by("createdDate").descending());
        return ResponseDto.builder()
                .status("success")
                .data(commentService.getCommentsByPrivateDiary(user.getUid(), diaryId, pageInfo, pageable))
                .build();
    }
}
