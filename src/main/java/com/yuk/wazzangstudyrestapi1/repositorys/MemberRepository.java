package com.yuk.wazzangstudyrestapi1.repositorys;

import com.yuk.wazzangstudyrestapi1.domains.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<Member> findByEmailAndPasswd (String email, String passwd);

    Optional<Member> findByEmailAndActive (String email, boolean isActive);
}
