package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, JpaSpecificationExecutor<Diary> {

    Page<Diary> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Diary> findAllByIdIn(List<Long> id, Pageable pageable);

    Page<Diary> findAllByAccessLevelAndActive(String accessLevel, Boolean active, Pageable pageable);

    Optional<Diary> findByIdAndActive(Long id, Boolean active);

    boolean existsDiaryById(Long id);
}
