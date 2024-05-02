package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/svc/member/{followedId}/follow")
    public ResponseDto addFollow(@PathVariable Long followedId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(followService.save(followedId, userId))
                .build();
    }

    @DeleteMapping("/svc/member/{followedId}/follow")
    public ResponseDto deleteFollow(@PathVariable Long followedId) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUid();

        return ResponseDto.builder()
                .status("success")
                .data(followService.delete(followedId, userId))
                .build();
    }
}
