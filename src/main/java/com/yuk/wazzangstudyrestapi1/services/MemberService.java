package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Member;
import com.yuk.wazzangstudyrestapi1.dtos.*;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.MemberRepository;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.utils.EncryptUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtComponent jwtComponent;

    private final AuthenticationManager authenticationManager;

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(MemberRequestDto requestDto) {
        try {
            Member member = Member.builder()
                    .email(requestDto.getEmail())
                    .passwd(EncryptUtil.encryptSHA256(requestDto.getPasswd()))
                    .userrole(requestDto.getUserrole())
                    .profilePicture(requestDto.getProfilePicture())
                    .nickname(requestDto.getNickname())
                    .birthDate(requestDto.getBirthDate())
                    .name(requestDto.getName())
                    .gender(requestDto.getGender())
                    .active(requestDto.getActive())
                    .build();

            return memberRepository.save(member).getId();
        } catch (EntityExistsException | ConstraintViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }

    }

    public LoginResultDto login(LoginRequestDto dto) {
        LoginResultDto rtnDto = LoginResultDto.builder().build();
        try {
            Authentication auth = authenticate(dto.getEmail(), EncryptUtil.encryptSHA256(dto.getPasswd()));
            SecurityUserDetail detail = (SecurityUserDetail) auth.getPrincipal();
            String strRoles = detail.getAuthorities().toString().replaceAll("\\s+","");
            strRoles = strRoles.substring(1,strRoles.length()-1);

            String jtoken = jwtComponent.createToken(detail.getUsername(), strRoles);

            rtnDto.setEmail(dto.getEmail());
            rtnDto.setJtoken(jtoken);
            rtnDto.setUserrole(strRoles);
            rtnDto.setId(detail.getUid());

            updateUserToken(detail.getUid(),jtoken);

            return rtnDto;
        } catch (Exception e) {
            return null;
        }
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            System.out.println("user disabled");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("invalid credentials");
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            System.out.println("Exception e : "+e);
            throw new Exception("Unknown Exception", e);
        }
    }

    private void updateUserToken(Long uid, String jtoken) {
        Optional<Member> opt = memberRepository.findById(uid);
        if(opt.isPresent()) {
            Member row = opt.get();
            row.setJtoken(jtoken);

            memberRepository.save(row);
        }
    }

    @Transactional
    public Long update(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.update(
                requestDto.getProfilePicture() != null ? requestDto.getProfilePicture() : member.getProfilePicture(),
                requestDto.getNickname() != null ? requestDto.getNickname() : member.getNickname(),
                requestDto.getActive() != null ? requestDto.getActive() : member.getActive()
        );

        return id;
    }

    public List<MemberResponseDto> findAllMembers () {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::from)
                .collect(Collectors.toList());
    }

    public MemberResponseDto findById (Long id) {
        Member entity = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다. id= " + id));
        return MemberResponseDto.from(entity);
    }

    public boolean existsByEmail (String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public Long setAdminRole (Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다. id= " + id));
        member.setAdminRole();
        return id;
    }
}
