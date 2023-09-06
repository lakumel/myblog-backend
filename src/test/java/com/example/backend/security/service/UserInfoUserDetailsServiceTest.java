package com.example.backend.security.service;

import com.example.backend.controller.dto.RequestRegisterUser;
import com.example.backend.domain.UserInfo;
import com.example.backend.domain.enums.Role;
import com.example.backend.repository.UserInfoRepository;
import com.example.backend.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("local")
class UserInfoUserDetailsServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    private PasswordEncoder passwordEncoder;

    private UserInfoUserDetailsService userInfoUserDetailsService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        this.userInfoUserDetailsService = new UserInfoUserDetailsService(userInfoRepository);
    }

    @Test
    void loadUserInfo() {
        String username = "tester";
        String email = "test@test.com";
        String password = "1q2w3e4r!";

        UserInfo savedUser = UserInfo.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .userRole(Role.READ)
                .build();
        savedUser.changeState(true);

        given(userInfoRepository.findUserInfoByEmail(email)).willReturn(Optional.of(savedUser));
        UserDetails loadUserByUsername = userInfoUserDetailsService.loadUserByUsername(email);

        assertThat(loadUserByUsername.getUsername()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, loadUserByUsername.getPassword())).isTrue();
        assertThat(loadUserByUsername.getAuthorities().toArray()[0].toString()).isEqualTo(Role.READ.getRole());

        verify(userInfoRepository).findUserInfoByEmail(email);
    }

}