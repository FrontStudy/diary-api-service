package com.yuk.wazzangstudyrestapi1.services;

import com.yuk.wazzangstudyrestapi1.repositorys.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
}
