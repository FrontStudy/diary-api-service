package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Member;
import com.yuk.wazzangstudyrestapi1.dtos.*;
import com.yuk.wazzangstudyrestapi1.repositorys.MemberRepository;
import com.yuk.wazzangstudyrestapi1.utils.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(MemberRequestDto requestDto) {
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .passwd(EncryptUtil.encryptSHA256(requestDto.getPasswd()))
                .userrole(requestDto.getUserrole())
                .profilePicture(requestDto.getProfilePicture())
                .nickname(requestDto.getNickname())
                .birthDate(requestDto.getBirthDate())
                .name(requestDto.getName())
                .gender(requestDto.getGender())
                .isActive(requestDto.isActive())
                .build();

        return memberRepository.save(member).getId();
    }

    @Transactional
    public LoginResultDto login (LoginRequestDto dto) {
        Optional<Member> optMember =  memberRepository.findByEmailAndPasswd(dto.getEmail(), EncryptUtil.encryptSHA256(dto.getPasswd()));
        if(optMember.isPresent()) {
            Member member = optMember.get();
            return LoginResultDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .userrole(member.getUserrole())
                    .build();
        }
        return null;
    }

    @Transactional
    public Long update(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다. id= " + id));
        System.out.println(requestDto.isActive());
        member.update(
                requestDto.getProfilePicture(),
                requestDto.getNickname(),
                requestDto.isActive()
        );

        return id;
    }

    public MemberResponseDto findById (Long id) {
        Member entity = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다. id= " + id));
        return MemberResponseDto.from(entity);
    }

    public boolean existsByEmail (String email) {
        return memberRepository.existsByEmail(email);
    }
}
