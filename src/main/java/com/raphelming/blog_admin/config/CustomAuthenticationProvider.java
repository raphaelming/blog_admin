package com.raphelming.blog_admin.config;

import com.raphelming.blog_admin.domain.MemberDetailVO;
import com.raphelming.blog_admin.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();
        String password = (String) token.getCredentials();
        MemberDetailVO memberDetailVO = (MemberDetailVO) memberService.loadUserByUsername(name);

        if (!passwordEncoder.matches(password, memberDetailVO.getPassword())) {
            throw new BadCredentialsException(memberDetailVO.getUsername() + "잘못된 비밀번호");
        }
        return new UsernamePasswordAuthenticationToken(memberDetailVO, password, memberDetailVO.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
