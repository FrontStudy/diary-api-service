package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Follow;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.FollowRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public boolean save(Long followedId, Long memberId) {
        if(!memberRepository.existsMemberById(followedId)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        //ToDo : 내가 읽을 권한이 있는 일기인지 체크 필요

        try {
            Follow follow = new Follow(followedId, memberId);
            followRepository.save(follow);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.CONFLICT_FOLLOW_STATE);
        }
        return true;
    }

    @Transactional
    public boolean delete(Long followedId, Long memberId) {
        if(followRepository.existsFollowByFollowedIdAndFollowerId(followedId, memberId)) {
            followRepository.deleteFollowByFollowedIdAndFollowerId(followedId, memberId);
            return false;
        }
        throw new CustomException(ErrorCode.CONFLICT_FOLLOW_STATE);
    }
}
