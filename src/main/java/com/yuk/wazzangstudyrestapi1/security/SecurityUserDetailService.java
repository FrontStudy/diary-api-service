package com.yuk.wazzangstudyrestapi1.security;

import com.yuk.wazzangstudyrestapi1.domains.Member;
import com.yuk.wazzangstudyrestapi1.repositorys.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(SecurityContextHolder.getContext().getAuthentication()==null) {
             System.out.println("인증정보 없는 경우 : login 외의 접근.");
        } else {
             System.out.println("인증정보 있는 경우 : login 시 authenticate() 호출 후");
        }

        Optional<Member> optItem = memberRepository.findByEmailAndActive(username,true);
        Member item = null;

        if(optItem.isPresent()) {
            item = optItem.get();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<String> authList = Arrays.asList(item.getUserrole().split(","));

        if (item.getEmail().equals(username)) {
            System.out.println("get and match email");
            return SecurityUserDetail.builder()
                    .username(item.getEmail())
                    .password("{noop}"+item.getPasswd())
                    .btoken(item.getJtoken())
                    .roles(authList)
                    .uid(item.getId())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
