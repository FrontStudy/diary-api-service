package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.dtos.DiaryRequestDto;
import com.yuk.wazzangstudyrestapi1.dtos.DiaryUpdateRequestDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

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
        if(!Objects.equals(diary.getMemberId(), memberId)) {
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
}
