package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.repositorys.DiaryShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryShareService {
    private final DiaryShareRepository diaryShareRepository;
}
