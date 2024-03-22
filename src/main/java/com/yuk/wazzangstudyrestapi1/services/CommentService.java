package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Comment;
import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentResponseDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.CommentRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiaryRepository diaryRepository;

    public Long save(CommentRequestDto dto, Long uid) {
        try {
            Comment comment = Comment.builder()
                    .memberId(uid)
                    .diaryId(dto.getDiaryId())
                    .content(dto.getContent())
                    .build();

            return commentRepository.save(comment).getId();
        } catch (EntityExistsException | ConstraintViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<CommentResponseDto> getCommentByPubDiary(Long diaryId) {
        Diary diary = diaryRepository.findByIdAndActive(diaryId, true)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        if(!Objects.equals(diary.getAccessLevel(), "public")) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        List<Comment> comments = commentRepository.findCommentsByDiaryIdAndActive(diaryId, true);

        return comments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }
}
