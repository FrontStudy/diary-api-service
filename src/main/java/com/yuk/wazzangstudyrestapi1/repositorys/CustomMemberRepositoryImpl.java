package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.dtos.member.MemberAdminResponseDto;
import com.yuk.wazzangstudyrestapi1.dtos.member.RequestMemberListDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<MemberAdminResponseDto> findMembersByConditionsWithCounts(RequestMemberListDto dto, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT new com.yuk.wazzangstudyrestapi1.dtos.member.MemberAdminResponseDto(" +
                "m.createdDate, m.modifiedDate, m.id, m.profilePicture, " +
                "m.nickname, m.birthDate, m.name, m.gender, m.email, " +
                "m.withdrawalDate, m.active, " +
                "(SELECT COUNT(d) FROM Diary d WHERE d.memberId = m.id AND d.active = true), " +
                "(SELECT COUNT(c) FROM Comment c WHERE c.memberId = m.id AND c.active = true)) " +
                "FROM Member m ");

        List<MemberAdminResponseDto> list = makeQueryWithSearchConditions(dto, jpql, " GROUP BY m.id", MemberAdminResponseDto.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        StringBuilder countJpql = new StringBuilder("SELECT COUNT(DISTINCT m.id) " +
                "FROM Member m ");

        Long totalCount = (Long) makeQueryWithSearchConditions(dto, countJpql, "", Long.class).getSingleResult();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return new PageImpl<>(list, pageRequest, totalCount);
    }

    private <T> Query makeQueryWithSearchConditions(RequestMemberListDto dto, StringBuilder jpql, String suffixQuery, Class<T> aClass) {
        boolean firstCondition = true;

        if (jpql.isEmpty()) return null;

        if (dto.getMemberId() != null) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" m.id = :memberId");
            firstCondition = false;
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" LOWER(m.email) LIKE LOWER(:email)");
            firstCondition = false;
        }

        if (dto.getUserrole() != null && !dto.getUserrole().isEmpty()) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" m.userrole = :userrole");
            firstCondition = false;
        }

        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" LOWER(m.nickname) LIKE LOWER(:nickname)");
            firstCondition = false;
        }

        if (dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" m.birthDate = :birthDate");
            firstCondition = false;
        }

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" LOWER(m.name) LIKE LOWER(:name)");
            firstCondition = false;
        }

        if (dto.getGender() != null && !dto.getGender().isEmpty()) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" m.gender = :gender");
            firstCondition = false;
        }

        if (dto.getActive() != null) {
            jpql.append(firstCondition ? " WHERE" : " AND");
            jpql.append(" m.active = :active");
            firstCondition = false;
        }

        if(!suffixQuery.isEmpty()) {
            jpql.append(suffixQuery);
        }
        Query query = entityManager.createQuery(jpql.toString(), aClass);

        if (dto.getMemberId() != null) {
            query.setParameter("memberId", dto.getMemberId());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            query.setParameter("email", "%" + dto.getEmail() + "%");
        }

        if (dto.getUserrole() != null && !dto.getUserrole().isEmpty()) {
            query.setParameter("userrole", dto.getUserrole());
        }

        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            query.setParameter("nickname", "%" + dto.getNickname() + "%");
        }

        if (dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()) {
            query.setParameter("birthDate", dto.getBirthDate());
        }

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            query.setParameter("name", "%" + dto.getName() + "%");
        }

        if (dto.getGender() != null && !dto.getGender().isEmpty()) {
            query.setParameter("gender", dto.getGender());
        }

        if (dto.getActive() != null) {
            query.setParameter("active", dto.getActive());
        }

        return query;
    }
}