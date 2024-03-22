package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.domains.Comment;
import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentResponseDto;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public ResponseDto commentListByPubDiary(@PathVariable Long diaryId) {
        List<CommentResponseDto> comments = commentService.getCommentByPubDiary(diaryId);
        return ResponseDto.builder()
                .status("success")
                .data(comments)
                .build();
    }
}
