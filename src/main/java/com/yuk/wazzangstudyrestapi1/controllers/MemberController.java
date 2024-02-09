package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.*;
import com.yuk.wazzangstudyrestapi1.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public Long memberSave(@RequestBody MemberRequestDto requestDto) {
        return memberService.save(requestDto);
    }

    @PostMapping("/api/v1/login")
    public LoginResultDto login (@RequestBody LoginRequestDto dto) {
        return memberService.login(dto);
    }

    @PutMapping("api/v1/members/{id}")
    public Long update(@PathVariable Long id, @RequestBody MemberUpdateRequestDto requestDto) {
        return memberService.update(id, requestDto);
    }

    @GetMapping("api/v1/members/{id}")
    public MemberResponseDto findById (@PathVariable Long id) {
        return memberService.findById(id);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean exists = memberService.existsByEmail(email);
        return ResponseEntity.ok(!exists);
    }

}
