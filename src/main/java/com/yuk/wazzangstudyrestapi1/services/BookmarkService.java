package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.domains.Bookmark;
import com.yuk.wazzangstudyrestapi1.exceptions.CustomException;
import com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode;
import com.yuk.wazzangstudyrestapi1.repositorys.BookmarkRepository;
import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final DiaryRepository diaryRepository;

    public boolean save(Long diaryId, Long memberId) {
        if(!diaryRepository.existsDiaryById(diaryId)) {
            throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
        }
        try {
            Bookmark bookmark = new Bookmark(diaryId, memberId);
            bookmarkRepository.save(bookmark);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.CONFLICT_BOOKMARK_STATE);
        }
        return true;
    }

    @Transactional
    public boolean delete(Long diaryId, Long memberId) {
        if(bookmarkRepository.existsBookmarkByDiaryIdAndMemberId(diaryId, memberId)) {
            bookmarkRepository.deleteBookmarkByDiaryIdAndMemberId(diaryId, memberId);
            return false;
        }
        throw new CustomException(ErrorCode.CONFLICT_BOOKMARK_STATE);
    }
}
