package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
