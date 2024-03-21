package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.dtos.diary.DiaryListAdminRequestDto;
import org.springframework.data.jpa.domain.Specification;

public class DiarySpecifications {
    public static Specification<Diary> withDiaryListAdminRequestDto(DiaryListAdminRequestDto dto) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    dto.getMemberId() != null ? criteriaBuilder.equal(root.get("id"), dto.getMemberId()) : criteriaBuilder.conjunction(),
                    dto.getTitle() != null ? criteriaBuilder.equal(root.get("title"), dto.getTitle()) : criteriaBuilder.conjunction(),
                    dto.getAccessLevel() != null ? criteriaBuilder.equal(root.get("accessLevel"), dto.getAccessLevel()) : criteriaBuilder.conjunction(),
                    dto.getActive() != null ? criteriaBuilder.equal(root.get("active"), dto.getActive()) : criteriaBuilder.conjunction()
            );
        };
    }
}