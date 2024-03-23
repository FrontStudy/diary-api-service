package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Likes;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final DiaryRepository diaryRepository;
    private final LikesRepository likesRepository;

    public boolean save(Long diaryId, Long userId) {
        if(!diaryRepository.existsDiaryById(diaryId)) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        //ToDo : 내가 읽을 권한이 있는 일기인지 체크 필요
        try {
            Likes likes = new Likes(diaryId, userId);
            likesRepository.save(likes);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.CONFLICT_LIKES_STATE);
        }
        // 좋아요 여부 반환
        return true;
    }

    @Transactional
    public boolean delete(Long diaryId, Long userId) {
        if(likesRepository.existsLikesByDiaryIdAndMemberId(diaryId, userId)) {
            likesRepository.deleteLikesByDiaryIdAndMemberId(diaryId, userId);
            // 좋아요 여부 반환
            return false;
        }
        throw new CustomException(ErrorCode.CONFLICT_LIKES_STATE);
    }
}
