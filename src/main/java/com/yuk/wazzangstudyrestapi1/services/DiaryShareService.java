package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.DiaryShare;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryShareService {

    private final DiaryRepository diaryRepository;
    private final DiaryShareRepository diaryShareRepository;

    @Transactional
    public int updateDiaryShares(Long diaryId, Long userId, List<Long> newMemberIds) {
        System.out.println("DiaryShareService.updateDiaryShares");

        // 유효성 검사
        if (newMemberIds.contains(userId)) {
            throw new CustomException(ErrorCode.SHARE_DIARY_MYSELF);
        }

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        if (!Objects.equals(diary.getMemberId(), userId)) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        List<DiaryShare> existingShares = diaryShareRepository.findAllByDiaryId(diaryId);
        Set<Long> existingMemberIds = existingShares.stream()
                .map(DiaryShare::getMemberId)
                .collect(Collectors.toSet());

        List<DiaryShare> sharesToDelete = existingShares.stream()
                .filter(share -> !newMemberIds.contains(share.getMemberId()))
                .collect(Collectors.toList());

        Set<Long> memberIdsToAdd = newMemberIds.stream()
                .filter(id -> !existingMemberIds.contains(id))
                .collect(Collectors.toSet());

        diaryShareRepository.deleteAll(sharesToDelete);

        List<DiaryShare> sharesToAdd = memberIdsToAdd.stream()
                .map(memberId -> new DiaryShare(diaryId, memberId))
                .collect(Collectors.toList());
        diaryShareRepository.saveAll(sharesToAdd);

        return diaryShareRepository.findAllByDiaryId(diaryId).size();
    }

    public List<Long> getMembersByDiaryId(Long userId, Long diaryId) {
        System.out.println("DiaryShareService.getMembersByDiaryId");
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        if(!Objects.equals(diary.getMemberId(), userId)) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        return diaryShareRepository.findAllByDiaryId(diaryId).stream().map(
                DiaryShare::getMemberId
        ).toList();
    }

    public List<Long> getSharedDiariesByMemberId(Long memberId) {
        System.out.println("DiaryShareService.getSharedDiariesByMemberId");
        return diaryShareRepository.findAllByMemberId(memberId).stream().map(
                DiaryShare::getDiaryId
        ).toList();
    }
}
