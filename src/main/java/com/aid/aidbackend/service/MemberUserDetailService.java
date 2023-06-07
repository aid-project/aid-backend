package com.aid.aidbackend.service;

import com.aid.aidbackend.entity.Member;
import com.aid.aidbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 여기서 사용되는 username은 UserDetails 클래스에서 사용되는 username이다.
     * 본 프로젝트에서 User에 해당하는 도메인은 Member이다.
     * 따라서, username은 Member field의 email이다.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + "는 존재하지 않습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
