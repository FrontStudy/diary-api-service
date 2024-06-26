package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.Member;
import com.yuk.wazzangstudyrestapi1.dtos.*;
import com.yuk.wazzangstudyrestapi1.dtos.member.*;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.*;
import com.yuk.wazzangstudyrestapi1.security.SecurityUserDetail;
import com.yuk.wazzangstudyrestapi1.utils.EncryptUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final DiaryService diaryService;

    private final JwtComponent jwtComponent;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryShareRepository diaryShareRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikesRepository likesRepository;
    private final FollowRepository followRepository;

    @Transactional
    public LoginResultDto save(MemberRequestDto requestDto) {
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

            memberRepository.save(member);

            LoginRequestDto loginReqDto = new LoginRequestDto();
            loginReqDto.setEmail(requestDto.getEmail());
            loginReqDto.setPasswd(requestDto.getPasswd());
            return login(loginReqDto);

        } catch (EntityExistsException | ConstraintViolationException | DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }

    }

    public LoginResultDto login(LoginRequestDto dto) {

        memberRepository.findByEmailAndActive(dto.getEmail(), true).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        LoginResultDto rtnDto = LoginResultDto.builder().build();
        Authentication auth = authenticate(dto.getEmail(), EncryptUtil.encryptSHA256(dto.getPasswd()));
        SecurityUserDetail detail = (SecurityUserDetail) auth.getPrincipal();
        String strRoles = detail.getAuthorities().toString().replaceAll("\\s+","");
        strRoles = strRoles.substring(1,strRoles.length()-1);

        String jtoken = jwtComponent.createToken(detail.getUsername(), strRoles);

        rtnDto.setEmail(dto.getEmail());
        rtnDto.setJtoken(jtoken);
        rtnDto.setUserrole(strRoles);
        rtnDto.setId(detail.getUid());
        rtnDto.setGender(detail.getGender());
        rtnDto.setName(detail.getName());
        rtnDto.setNickname(detail.getNickname());
        rtnDto.setBirthDate(detail.getBirthDate());
        rtnDto.setProfilePicture(detail.getProfilePicture());

        updateUserToken(detail.getUid(),jtoken);

        return rtnDto;
    }

    private Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new CustomException(ErrorCode.MEMBER_DISABLED);
        } catch (UsernameNotFoundException e) {
            System.out.println("invalid email");
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        } catch (BadCredentialsException e) {
            System.out.println("invalid credentials");
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        } catch (Exception e) {
            System.out.println("Exception e : "+e);
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
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
                requestDto.getNickname() != null ? requestDto.getNickname() : member.getNickname()
        );

        return id;
    }

    @Transactional
    public Long updateByAdmin(Long id, MemberUpdateAdminRequestDto requestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateByAdmin(
                requestDto.getProfilePicture() != null ? requestDto.getProfilePicture() : member.getProfilePicture(),
                requestDto.getNickname() != null ? requestDto.getNickname() : member.getNickname(),
                requestDto.getUserrole() != null ? requestDto.getUserrole() : member.getUserrole()
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

    public boolean existsByNickname (String nickname) { return memberRepository.existsByNickname(nickname); }

    @Transactional
    public Long setAdminRole (Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다. id= " + id));
        member.setAdminRole();
        return id;
    }

    public List<MemberAdminResponseDto> findWithSearchConditions (RequestMemberListDto dto, PageInfoDto pageDto) {
        Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
        Page<MemberAdminResponseDto> page = memberRepository.findMembersByConditionsWithCounts(dto, pageable);

        pageDto.setTotalPages((long) page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());

        return page.getContent();
    }

    @Transactional
    public boolean deactivate(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.deactivate();
        bookmarkRepository.deleteAllByMemberId(memberId);
        diaryShareRepository.deleteAllByMemberId(memberId);
        likesRepository.deleteAllByMemberId(memberId);
        followRepository.deleteAllByFollowedId(memberId);
        followRepository.deleteAllByFollowerId(memberId);

        List<Diary> diaries = diaryRepository.findDiaryByMemberId(memberId);
        diaries.forEach(
                diary -> {
                    try {
                        diaryService.deactivateDiary(diary.getId());
                    } catch(Exception e) {
                        System.out.println("Error during delete diary "+diary.getId());
                    }
                }
        );
        return true;
    }

    public MemberDetailInfoDto getDetailInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Long diariesCnt = diaryRepository.countByActiveAndMemberId(true, id);
        Long followerCnt = followRepository.countByFollowedId(id);
        Long followingCnt = followRepository.countByFollowerId(id);

        System.out.println("follower : " + followerCnt.toString());

        return MemberDetailInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .profilePicture(member.getProfilePicture())
                .nickname(member.getNickname())
                .name(member.getName())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .diaryCount(diariesCnt)
                .followerCount(followerCnt)
                .followingCount(followingCnt)
                .build();

    }
}
