package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Comment;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentRequestDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.CommentRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

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
}
