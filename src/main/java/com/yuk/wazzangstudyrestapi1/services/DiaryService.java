package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.repositorys.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
}
