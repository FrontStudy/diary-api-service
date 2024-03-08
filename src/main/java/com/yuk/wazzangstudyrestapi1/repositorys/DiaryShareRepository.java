package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.DiaryShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryShareRepository  extends JpaRepository<DiaryShare, Long> {

    List<DiaryShare> findAllByMemberId(Long member_id);
    List<DiaryShare> findAllByDiaryId(Long diary_id);
}
