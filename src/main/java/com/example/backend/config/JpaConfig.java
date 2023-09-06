package com.example.backend.config;

import com.example.backend.domain.UserInfo;
import com.example.backend.exception.NotAuthenticatedUser;
import com.example.backend.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
@Slf4j
public class JpaConfig {
    private final UserInfoRepository userInfoRepository;

    @Bean
    public AuditorAware<UserInfo> auditorProvider(){
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication == null){
                return Optional.ofNullable(null);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<UserInfo> optionalUserInfo = userInfoRepository.findUserInfoByEmail(userDetails.getUsername());
            return Optional.of(optionalUserInfo.get());
        };
    }
}

