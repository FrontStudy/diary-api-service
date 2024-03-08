package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.DiaryShare;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiaryShareService {

    private final DiaryRepository diaryRepository;
    private final DiaryShareRepository diaryShareRepository;

    public int addDiaryShares(Long diaryId, Long userId, List<Long> memberIds) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
        if(!Objects.equals(diary.getMemberId(), userId)) {
            throw new CustomException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        if(memberIds.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_DIARYSHARE_MEMBER);
        }

        List<DiaryShare> diaryShares;
        diaryShares = memberIds.stream().map(
                (memberId) -> new DiaryShare(diaryId, memberId)
        ).toList();

        return diaryShareRepository.saveAll(diaryShares).size();
    }
}
