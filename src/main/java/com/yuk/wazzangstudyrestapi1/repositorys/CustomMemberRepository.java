package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.dtos.member.MemberAdminResponseDto;
import com.yuk.wazzangstudyrestapi1.dtos.member.RequestMemberListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomMemberRepository {
    Page<MemberAdminResponseDto> findMembersByConditionsWithCounts(RequestMemberListDto dto, Pageable pageable);
}
