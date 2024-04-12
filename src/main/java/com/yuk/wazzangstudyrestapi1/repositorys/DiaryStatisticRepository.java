package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.DiaryStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiaryStatisticRepository extends JpaRepository<DiaryStatistic, Long>  {
    Optional<DiaryStatistic> findDiaryStatisticByDiaryId(Long diaryId);
    boolean deleteAllByDiaryId(Long diaryId);
}
