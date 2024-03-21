package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Member;
import com.yuk.wazzangstudyrestapi1.dtos.member.RequestMemberListDto;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecifications {

    public static Specification<Member> withRequestMemberListDto(RequestMemberListDto requestMemberListDto) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    requestMemberListDto.getMemberId() != null ? criteriaBuilder.equal(root.get("id"), requestMemberListDto.getMemberId()) : criteriaBuilder.conjunction(),
                    requestMemberListDto.getEmail() != null ? criteriaBuilder.like(root.get("email"), "%" + requestMemberListDto.getEmail() + "%") : criteriaBuilder.conjunction(),
                    requestMemberListDto.getUserrole() != null ? criteriaBuilder.equal(root.get("userrole"), requestMemberListDto.getUserrole()) : criteriaBuilder.conjunction(),
                    requestMemberListDto.getNickname() != null ? criteriaBuilder.like(root.get("nickname"), "%" + requestMemberListDto.getNickname() + "%") : criteriaBuilder.conjunction(),
                    requestMemberListDto.getBirthDate() != null ? criteriaBuilder.equal(root.get("birthDate"), requestMemberListDto.getBirthDate()) : criteriaBuilder.conjunction(),
                    requestMemberListDto.getName() != null ? criteriaBuilder.like(root.get("name"), "%" + requestMemberListDto.getName() + "%") : criteriaBuilder.conjunction(),
                    requestMemberListDto.getGender() != null ? criteriaBuilder.equal(root.get("gender"), requestMemberListDto.getGender()) : criteriaBuilder.conjunction(),
                    requestMemberListDto.getActive() != null ? criteriaBuilder.equal(root.get("active"), requestMemberListDto.getActive()) : criteriaBuilder.conjunction()
            );
        };
    }
}