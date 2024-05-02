package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsLikesByDiaryIdAndMemberId(Long diaryId, Long memberId);
    void deleteLikesByDiaryIdAndMemberId(Long diaryId, Long memberId);
    void deleteByDiaryId(Long diaryId);
    void deleteAllByMemberId(Long diaryId);

    Long countLikesByDiaryId(Long diaryId);
}
