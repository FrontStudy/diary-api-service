package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Comment;
import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.DiaryShare;
import com.yuk.wazzangstudyrestapi1.domains.Member;
import com.yuk.wazzangstudyrestapi1.dtos.PageInfoDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.comment.CommentResponseDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.CommentRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryShareRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryShareRepository diaryShareRepository;
    private final MemberRepository memberRepository;

    public Long save(CommentRequestDto dto, Long uid) {

        //ToDo : 내가 읽을 권한이 있는 일기인지 체크 필요

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

    public List<CommentResponseDto> getCommentByPubDiary(Long diaryId, PageInfoDto pageInfo, Pageable pageable) {
        Diary diary = diaryRepository.findByIdAndActive(diaryId, true)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        if(!Objects.equals(diary.getAccessLevel(), "public")) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        return getCommentsForDiary(diaryId, pageInfo, pageable);
    }

    public List<CommentResponseDto> getCommentsByPrivateDiary(Long memberId, Long diaryId, PageInfoDto pageInfo, Pageable pageable) {
        Diary diary = diaryRepository.findByIdAndActive(diaryId, true)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        // 공개 다이어리이거나, 다이어리 소유자이거나, 공유받은 다이어리인 경우
        if (isPublicDiary(diary) || isDiaryOwner(diary, memberId) || isSharedDiary(diary, memberId)) {
            return getCommentsForDiary(diaryId, pageInfo, pageable);
        }

        // 위 조건에 모두 해당하지 않는 경우, 권한이 없는 것으로 판단
        throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
    }

    private boolean isPublicDiary(Diary diary) {
        return Objects.equals(diary.getAccessLevel(), "public");
    }

    private boolean isDiaryOwner(Diary diary, Long memberId) {
        return Objects.equals(diary.getMemberId(), memberId);
    }

    private boolean isSharedDiary(Diary diary, Long memberId) {
        if (Objects.equals(diary.getAccessLevel(), "private")) {
            return diaryShareRepository.findAllByDiaryId(diary.getId()).stream()
                    .map(DiaryShare::getMemberId)
                    .anyMatch(id -> id.equals(memberId));
        }
        return false;
    }

    private List<CommentResponseDto> getCommentsForDiary(Long diaryId, PageInfoDto pageInfo, Pageable pageable) {
        Page<Comment> comments = commentRepository.findCommentsByDiaryIdAndActiveOrderByCreatedDateDesc(diaryId, true, pageable);

        pageInfo.setTotalPages((long) comments.getTotalPages());
        pageInfo.setTotalElements(comments.getTotalElements());

        List<CommentResponseDto> rDtos = comments.stream()
                .map((comment) -> {
                    CommentResponseDto rdto = CommentResponseDto.from(comment);
                    Optional<Member> author = memberRepository.findByIdAndActive(comment.getMemberId(), true);
                    if(author.isEmpty()) {
                        return null;
                    } else {
                        rdto.setNickname(author.get().getNickname());
                        rdto.setProfilePicture(author.get().getProfilePicture());
                        return rdto;
                    }
                }).filter((Objects::nonNull))
                .toList();
        return rDtos;
    }
}
