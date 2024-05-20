package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, JpaSpecificationExecutor<Diary> {

    List<Diary> findDiaryByMemberId(Long memberId);

    Page<Diary> findAllByMemberIdOrderByCreatedDateDesc(Long memberId, Pageable pageable);
    Page<Diary> findAllByIdInOrderByCreatedDateDesc(List<Long> id, Pageable pageable);

    Page<Diary> findAllByAccessLevelAndActiveOrderByCreatedDateDesc(String accessLevel, Boolean active, Pageable pageable);

    Page<Diary> findAllByAccessLevelAndActive(String accessLevel, Boolean active, Pageable pageable);

    Optional<Diary> findByIdAndActive(Long id, Boolean active);

    boolean existsDiaryById(Long id);

    Page<Diary> findDiariesByIdInAndActiveOrderByCreatedDateDesc(List<Long> id, Boolean active, Pageable pageable);

    @Query(
            value = "SELECT d.*, COALESCE(s.read_count, 0) as read_count " +
                    "FROM diary d " +
                    "LEFT JOIN diary_statistic s ON d.id = s.diary_id " +
                    "WHERE d.access_level = :accessLevel AND d.active = :active " +
                    "ORDER BY COALESCE(s.read_count, 0) DESC, d.created_date DESC",
            countQuery = "SELECT COUNT(d.id) " +
                    "FROM diary d " +
                    "LEFT JOIN diary_statistic s ON d.id = s.diary_id " +
                    "WHERE d.access_level = :accessLevel AND d.active = :active",
            nativeQuery = true
    )
    Page<Diary> findAllByAccessLevelAndActiveWithStatistic(String accessLevel, Boolean active, Pageable pageable);

    @Query(
            value = "SELECT d.* " +
                    "FROM diary d " +
                    "LEFT JOIN (SELECT diary_id, COUNT(*) as like_count FROM likes GROUP BY diary_id) l " +
                    "ON d.id = l.diary_id " +
                    "WHERE d.access_level = :accessLevel AND d.active = :active " +
                    "ORDER BY l.like_count DESC, d.created_date DESC",
            countQuery = "SELECT COUNT(d.id) " +
                    "FROM diary d " +
                    "LEFT JOIN (SELECT diary_id, COUNT(*) as like_count FROM likes GROUP BY diary_id) l " +
                    "ON d.id = l.diary_id " +
                    "WHERE d.access_level = :accessLevel AND d.active = :active",
            nativeQuery = true
    )
    Page<Diary> findAllByAccessLevelAndActiveOrderByLikes(@Param("accessLevel") String accessLevel, @Param("active") Boolean active, Pageable pageable);
}
