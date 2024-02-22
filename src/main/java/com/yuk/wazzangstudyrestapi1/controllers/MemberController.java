package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.*;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @CrossOrigin
    @PostMapping("/pub/members")
    public ResponseDto memberSave(@RequestBody MemberRequestDto dto) {
        if (dto.getEmail() != null && dto.getPasswd() != null) {
            if (dto.getEmail().isEmpty() || dto.getPasswd().isEmpty()) {
                throw new CustomException(ErrorCode.EMPTY_LOGIN_CREDENTIALS);
            } else {
                return ResponseDto.builder()
                        .status("success")
                        .data(memberService.save(dto))
                        .build();
            }
        } else {
            throw new CustomException(ErrorCode.EMPTY_LOGIN_CREDENTIALS);
        }
    }

    @CrossOrigin
    @PostMapping("/pub/login")
    public ResponseDto login (@RequestBody LoginRequestDto dto) {

        if (dto.getEmail() != null && dto.getPasswd() != null) {
            if (dto.getEmail().isEmpty() || dto.getPasswd().isEmpty()) {
                throw new CustomException(ErrorCode.EMPTY_LOGIN_CREDENTIALS);
            } else {
                LoginResultDto loginResult = memberService.login(dto);
                if (loginResult != null) {
                    return ResponseDto.builder()
                            .status("success")
                            .data(loginResult)
                            .build();
                } else {
                    throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                }
            }
        } else {
            throw new CustomException(ErrorCode.EMPTY_LOGIN_CREDENTIALS);
        }
    }

    @CrossOrigin
    @PutMapping("/svc/members/{id}")
    public ResponseDto update(@PathVariable Long id, @RequestBody MemberUpdateRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        dto.setId(user.getUid());

        return ResponseDto.builder()
                .status("success")
                .data(memberService.update(id, dto))
                .build();
    }

    @CrossOrigin
    @GetMapping("/pub/members")
    public ResponseDto getMembersAll () {
        return ResponseDto.builder()
                .status("success")
                .data(memberService.findAllMembers())
                .build();
    }

    @CrossOrigin
    @GetMapping("/pub/members/{id}")
    public MemberResponseDto findById (@PathVariable Long id) {
        return memberService.findById(id);
    }

    @CrossOrigin
    @GetMapping("/pub/check-email")
    public ResponseDto checkEmailDuplicate(@RequestParam(name = "email") String email) {
        boolean exists = memberService.existsByEmail(email);
        return ResponseDto.builder()
                .status("success")
                .data(exists)
                .build();
    }
}
