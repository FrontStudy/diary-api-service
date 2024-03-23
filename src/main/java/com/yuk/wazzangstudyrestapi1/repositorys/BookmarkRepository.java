package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsBookmarkByDiaryIdAndMemberId(Long diaryId, Long memberId);
    void deleteBookmarkByDiaryIdAndMemberId(Long diaryId, Long memberId);
    List<Bookmark> findBookmarksByMemberId(Long memberId);
}
