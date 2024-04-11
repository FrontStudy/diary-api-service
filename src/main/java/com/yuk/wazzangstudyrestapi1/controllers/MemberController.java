package com.yuk.wazzangstudyrestapi1.controllers;

import com.yuk.wazzangstudyrestapi1.dtos.*;
import com.yuk.wazzangstudyrestapi1.dtos.member.*;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

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

    @PutMapping("/svc/members/{id}")
    public ResponseDto update(@PathVariable Long id, @RequestBody MemberUpdateRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("user id : "+user.getUid());

        return ResponseDto.builder()
                .status("success")
                .data(memberService.update(id, dto))
                .build();
    }

    @PutMapping("/admin/members/{id}")
    public ResponseDto updateByAdmin(@PathVariable Long id, @RequestBody MemberUpdateAdminRequestDto dto) {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("user id : "+user.getUid());

        return ResponseDto.builder()
                .status("success")
                .data(memberService.updateByAdmin(id, dto))
                .build();
    }

    @GetMapping("/pub/members")
    public ResponseDto getMembersAll () {
        return ResponseDto.builder()
                .status("success")
                .data(memberService.findAllMembers())
                .build();
    }

    @PutMapping("/svc/setAdminRole")
    public ResponseDto setAdminRole () {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseDto.builder()
                .status("success")
                .data(memberService.setAdminRole(user.getUid()))
                .build();
    }

    @PostMapping("/admin/memberList")
    public ResponseDto getMemberList (@RequestBody RequestMemberListDto dto) {
        System.out.println("MemberController.getMemberList");
        System.out.println(dto.toString());

        PageInfoDto pageInfo = PageInfoDto.builder().build();

        List<MemberAdminResponseDto> resDtoList = memberService.findWithSearchConditions(dto, pageInfo);

        return ResponseDto.builder()
                .status("success")
                .data(resDtoList)
                .page(pageInfo)
                .build();
    }

    @GetMapping("/pub/members/{id}")
    public MemberResponseDto findById (@PathVariable Long id) {
        return memberService.findById(id);
    }

    @GetMapping("/pub/check-email")
    public ResponseDto checkEmailDuplicate(@RequestParam(name = "email") String email) {
        boolean exists = memberService.existsByEmail(email);
        return ResponseDto.builder()
                .status("success")
                .data(exists)
                .build();
    }

    @GetMapping("/pub/check-nickname")
    public ResponseDto checkNicknameDuplicate(@RequestParam(name = "nickname") String nickname) {
        boolean exists = memberService.existsByNickname(nickname);
        return ResponseDto.builder()
                .status("success")
                .data(exists)
                .build();
    }

    @PostMapping("/svc/member/deactivate")
    public ResponseDto deactivateMember() {
        SecurityUserDetail user = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseDto.builder()
                .status("success")
                .data(memberService.deactivate(user.getUid()))
                .build();
    }

    @PostMapping("/admin/member/deactivate/{id}")
    public ResponseDto deactivateMember(@PathVariable Long id) {

        return ResponseDto.builder()
                .status("success")
                .data(memberService.deactivate(id))
                .build();
    }
}
