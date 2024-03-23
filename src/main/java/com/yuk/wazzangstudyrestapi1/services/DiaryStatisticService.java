package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import com.yuk.wazzangstudyrestapi1.domains.DiaryStatistic;
import com.yuk.wazzangstudyrestapi1.dtos.ResponseDto;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryStatisticService {

    private final DiaryRepository diaryRepository;
    private final DiaryStatisticRepository diaryStatisticRepository;


    @Transactional
    public Long increaseCount(Long diaryId) {
        boolean isDiaryExists = diaryRepository.existsDiaryById(diaryId);
        if(!isDiaryExists) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }

        Optional<DiaryStatistic> opt = diaryStatisticRepository.findDiaryStatisticByDiaryId(diaryId);

        if(opt.isPresent()) {
            opt.get().increaseReadCount();
            return opt.get().getReadCount();
        } else {
            DiaryStatistic diaryStatistic = DiaryStatistic.builder()
                    .diaryId(diaryId)
                    .build();
            diaryStatisticRepository.save(diaryStatistic);
            return diaryStatistic.getReadCount();
        }
    }
}
