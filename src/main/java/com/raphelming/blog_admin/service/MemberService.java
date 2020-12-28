package com.raphelming.blog_admin.service;


import com.raphelming.blog_admin.domain.Member;
import com.raphelming.blog_admin.domain.MemberDetailVO;
import com.raphelming.blog_admin.domain.Role;
import com.raphelming.blog_admin.exception.MemberNotFoundException;
import com.raphelming.blog_admin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(Role.MEMBER);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByName(member.getName());
        if (findMembers.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return memberRepository.findByName(name).
                map(u -> new MemberDetailVO(u, Collections.singleton(
                        new SimpleGrantedAuthority(u.getRole().getValue()))))
                        .orElseThrow(() -> new UsernameNotFoundException("error"));
    }
}
