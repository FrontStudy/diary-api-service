package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.DiaryShare;
import com.yuk.wazzangstudyrestapi1.dtos.PageInfoDto;
import com.yuk.wazzangstudyrestapi1.dtos.diary.*;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryShareRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiarySpecifications;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryShareRepository diaryShareRepository;

    public Long save(DiaryRequestDto requestDto, Long memberId) {
        System.out.println("DiaryService.save");
        try {
            Diary diary = Diary.builder()
                    .memberId(memberId)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .imgid(requestDto.getImgid())
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
                requestDto.getImgid() != null ? requestDto.getImgid() : diary.getImgid(),
                requestDto.getAccessLevel() != null ? requestDto.getAccessLevel() : diary.getAccessLevel(),
                requestDto.getActive() != null ? requestDto.getActive() : diary.getActive()
        );
        return diaryId;
    }

    @Transactional
    public List<DiaryResponseDto> getListByMemberId(Long memberId, DiaryListRequestDto dto, PageInfoDto pageDto) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
            Page<Diary> page = diaryRepository.findAllByMemberId(memberId, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return page.getContent().stream()
                    .map(DiaryResponseDto::from)
                    .collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryResponseDto> getSharedDiaryListByMemberId(Long memberId, DiaryListRequestDto dto, PageInfoDto pageDto) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
            List<DiaryShare> diaryShares = diaryShareRepository.findAllByMemberId(memberId);
            List<Long> diaryIds = diaryShares.stream().map(
                    DiaryShare::getDiaryId
            ).toList();

            Page<Diary> diaries = diaryRepository.findAllByIdIn(diaryIds, pageable);

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
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
            Page<Diary> page = diaryRepository.findAll(spec, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return page.getContent().stream()
                    .map(DiaryResponseDto::from)
                    .collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public List<DiaryResponseDto> getpublicDiaryList(DiaryListRequestDto dto, PageInfoDto pageDto) {
        try {
            Pageable pageable = PageRequest.of(dto.getOffset(), dto.getSize());
            Page<Diary> page = diaryRepository.findAllByAccessLevelAndActive("public", true, pageable);

            pageDto.setTotalPages((long) page.getTotalPages());
            pageDto.setTotalElements(page.getTotalElements());

            return page.getContent().stream()
                    .map(DiaryResponseDto::from)
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            throw new CustomException(ErrorCode.PERSISTENCE_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }
}