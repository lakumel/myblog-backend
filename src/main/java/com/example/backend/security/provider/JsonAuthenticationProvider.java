package com.example.backend.security.provider;

import com.example.backend.security.service.UserInfoUserDetailsService;
import com.example.backend.security.token.JsonAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Slf4j
public class JsonAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails loadUserByUsername = null;
        try {
            loadUserByUsername = userDetailsService.loadUserByUsername(email);
        }catch (UsernameNotFoundException usernameNotFoundException){
            log.info("가입돼 있지 않은 Email 입니다. email : {}", email);
            throw usernameNotFoundException;
        }

        if(!passwordEncoder.matches(password, loadUserByUsername.getPassword())){
            log.info("비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new JsonAuthenticationToken(loadUserByUsername, authentication.getCredentials(), loadUserByUsername.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JsonAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
