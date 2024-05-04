package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsFollowByFollowedIdAndFollowerId(Long followedId, Long followerId);
    void deleteFollowByFollowedIdAndFollowerId(Long followedId, Long followerId);

    void deleteAllByFollowedId(Long followedId);

    void deleteAllByFollowerId(Long followerId);

    Long countByFollowedId(Long followedId);
}
