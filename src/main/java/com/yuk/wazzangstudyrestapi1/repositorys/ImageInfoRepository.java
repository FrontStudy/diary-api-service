package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long> {
    ArrayList<ImageInfo> findByIdIn(List<Long> id);

}