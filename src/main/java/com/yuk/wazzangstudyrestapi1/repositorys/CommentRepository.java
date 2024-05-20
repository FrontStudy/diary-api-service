package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentsByDiaryIdAndActiveOrderByCreatedDateDesc(Long diaryId, Boolean active, Pageable pageable);

    Long countCommentsByDiaryIdAndActive(Long diaryId, Boolean active);
}
