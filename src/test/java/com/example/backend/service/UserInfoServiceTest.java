package com.example.backend.service;

import com.example.backend.controller.dto.RequestRegisterUser;
import com.example.backend.domain.UserInfo;
import com.example.backend.domain.enums.Role;
import com.example.backend.repository.UserInfoRepository;
import com.example.backend.security.service.UserInfoUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    private PasswordEncoder passwordEncoder;
    @Mock
    private UserInfoService userInfoService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        this.userInfoService = new UserInfoService(passwordEncoder, userInfoRepository);
    }


    @Test
    void saveUserInfo() {
        String username = "tester";
        String email = "test@test.com";
        String password = "1q2w3e4r!";

        RequestRegisterUser registerUser = new RequestRegisterUser();
        registerUser.setUsername(username);
        registerUser.setEmail(email);
        registerUser.setPassword(password);

        UserInfo userInfo = UserInfo.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .userRole(Role.READ)
                .build();

        given(userInfoRepository.save(any(UserInfo.class))).willReturn(userInfo);
        UserInfo savedUserInfo = userInfoService.saveUser(registerUser);

        assertThat(savedUserInfo.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(savedUserInfo.getEmail()).isEqualTo(userInfo.getEmail());
        assertThat(savedUserInfo.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(savedUserInfo.getUserRole().getRole()).isEqualTo(userInfo.getUserRole().getRole());

        verify(userInfoRepository).save(any(UserInfo.class));
    }
}
