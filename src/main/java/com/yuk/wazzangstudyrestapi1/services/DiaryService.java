package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.*;
import com.yuk.wazzangstudyrestapi1.dtos.PageInfoDto;
import com.yuk.wazzangstudyrestapi1.dtos.diary.*;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryShareRepository diaryShareRepository;
    private final DiaryStatisticRepository diaryStatisticRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public Long save(DiaryRequestDto requestDto, Long memberId) {
        System.out.println("DiaryService.save");
        try {
            Diary diary = Diary.builder()
                    .memberId(memberId)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .imgUrl(requestDto.getImgUrl())
                    .accessLevel(requestDto.getAccessLevel())
                    .active(true)
                    .build();

            return diaryRepository.save(diary).getId();
        } catch (EntityExistsException | ConstraintViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public Long update(Long diaryId, Long memberId, DiaryUpdateRequestDto requestDto) {
        System.out.println("DiaryService.update");
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        if (!Objects.equals(diary.getMemberId(), memberId)) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        diary.update(
                requestDto.getTitle() != null ? requestDto.getTitle() : diary.getTitle(),
                requestDto.getContent() != null ? requestDto.getContent() : diary.getContent(),
                requestDto.getImgUrl() != null ? requestDto.getImgUrl() : diary.getImgUrl(),
                requestDto.getAccessLevel() != null ? requestDto.getAccessLevel() : diary.getAccessLevel(),
                requestDto.getActive() != null ? requestDto.getActive() : diary.getActive()
        );
        return diaryId;
    }

    public DiaryDetailsResponseDto getAccessibleDiaryDetailById(Long diaryId, Long memberId) {
        try {
            Diary diary = diaryRepository.findById(diaryId).orElseThrow(
                    () -> new CustomException(ErrorCode.DIARY_NOT_FOUND)
            );

            if(diary.getAccessLevel().equals("public")) {
                return convertDiaryToDetail(diary, memberId);
            } else if (checkAccessibleToPrivateDiary(diaryId, memberId)) {
                return convertDiaryToDetail(diary, memberId);
            } else {
                throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
            }

        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public List<DiaryResponseDto> getListByMemberId(Long memberId, DiaryListRequestDto dto, PageInfoDto pageDto) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize(), Sort.by("createdDate").descending());
            Page<Diary> page = diaryRepository.findAllByMemberIdOrderByCreatedDateDesc(memberId, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return convertPageToListWithViewCount(page);
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryResponseDto> getSharedDiaryListByMemberId(Long memberId, DiaryListRequestDto dto, PageInfoDto pageDto) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize(), Sort.by("createdDate").descending());
            List<DiaryShare> diaryShares = diaryShareRepository.findAllByMemberId(memberId);
            List<Long> diaryIds = diaryShares.stream().map(
                    DiaryShare::getDiaryId
            ).toList();

            Page<Diary> diaries = diaryRepository.findAllByIdInOrderByCreatedDateDesc(diaryIds, pageable);

            pageDto.setTotalPages((long) diaries.getTotalPages());
            pageDto.setTotalElements(diaries.getTotalElements());

            return diaries.getContent().stream()
                    .map(DiaryResponseDto::from)
                    .collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryResponseDto> getListAsAdmin(DiaryListAdminRequestDto dto, PageInfoDto pageDto) {
        try {
            Specification<Diary> spec = DiarySpecifications.withDiaryListAdminRequestDto(dto);
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize(), Sort.by("createdDate").descending());
            Page<Diary> page = diaryRepository.findAll(spec, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return convertPageToListWithViewCount(page);
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryResponseDto> getpublicDiaryList(DiaryListRequestDto dto, PageInfoDto pageDto) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
            Page<Diary> page = diaryRepository.findAllByAccessLevelAndActiveOrderByCreatedDateDesc("public", true, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return convertPageToListWithViewCount(page);

        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryDetailsResponseDto> getpublicDiaryDetailList(DiaryListRequestDto dto, PageInfoDto pageDto, Long memberId) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
            Page<Diary> page = diaryRepository.findAllByAccessLevelAndActiveOrderByCreatedDateDesc("public", true, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return convertPageToDetailsList(page, memberId);

        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryResponseDto> getBookmarkedDiaryList(Long memberId, DiaryListRequestDto dto, PageInfoDto pageDto) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByMemberId(memberId);
        if(bookmarks.isEmpty()) return null;

        Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
        Page<Diary> page = diaryRepository.findDiariesByIdInAndActiveOrderByCreatedDateDesc(bookmarks.stream().map(Bookmark::getDiaryId).toList(), true, pageable);

        pageDto.setTotalPages((long) page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());

        return convertPageToListWithViewCount(page);
    }

    @Transactional
    public boolean deactivateDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        diary.deactivate();
        diaryShareRepository.deleteAllByDiaryId(diaryId);
        diaryStatisticRepository.deleteAllByDiaryId(diaryId);
        bookmarkRepository.deleteAllByDiaryId(diaryId);
        likesRepository.deleteByDiaryId(diaryId);

        return true;
    }

    private DiaryDetailsResponseDto convertDiaryToDetail(Diary diary, Long memberId) {
        Long readCount = 0L;
        Optional<DiaryStatistic> optStat = diaryStatisticRepository.findDiaryStatisticByDiaryId(diary.getId());
        if(optStat.isPresent()) {
            readCount = optStat.get().getReadCount();
        }
        Long commentCount = commentRepository.countCommentsByDiaryIdAndActive(diary.getId(), true);
        Long likeCount = likesRepository.countLikesByDiaryId(diary.getId());
        Boolean isLiked = likesRepository.existsLikesByDiaryIdAndMemberId(diary.getId(), memberId);
        Boolean isBookmarked = bookmarkRepository.existsBookmarkByDiaryIdAndMemberId(diary.getId(), memberId);
        Boolean isFollowing = followRepository.existsFollowByFollowedIdAndFollowerId(diary.getMemberId(), memberId);

        DiaryDetailsResponseDto rDto = DiaryDetailsResponseDto.builder()
                .createdDate(diary.getCreatedDate())
                .modifiedDate(diary.getModifiedDate())
                .id(diary.getId())
                .memberId(diary.getMemberId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .imgUrl(diary.getImgUrl())
                .accessLevel(diary.getAccessLevel())
                .active(diary.getActive())
                .readCount(readCount)
                .commentCount(commentCount)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .isFollowing(isFollowing)
                .build();

        Member member = memberRepository.findById(diary.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        rDto.setAuthor(member);

        return rDto;

    }

    private List<DiaryResponseDto> convertPageToListWithViewCount(Page<Diary> page) {
        return page.getContent().stream()
                .map(diary -> {
                    DiaryResponseDto rDto = DiaryResponseDto.from(diary);
                    Optional<DiaryStatistic> opt = diaryStatisticRepository.findDiaryStatisticByDiaryId(diary.getId());
                    if(opt.isPresent()) {
                        rDto.setReadCount(opt.get().getReadCount());
                    } else {
                        rDto.setReadCount(0L);
                    }
                    return rDto;
                })
                .toList();
    }

    private List<DiaryDetailsResponseDto> convertPageToDetailsList(Page<Diary> page, Long memberId) {
        return page.getContent().stream()
                .map(diary -> {
                    return convertDiaryToDetail(diary, memberId);
                })
                .toList();
    }

    private boolean checkAccessibleToPrivateDiary(Long diaryId, Long memberId) {
        Optional<DiaryShare> diaryShare = diaryShareRepository.findByMemberIdAndDiaryId(memberId, diaryId);
        return diaryShare.isPresent();
    }
}