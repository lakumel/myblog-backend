package com.example.backend.service;

import com.example.backend.controller.dto.RequestRegisterUser;
import com.example.backend.domain.UserInfo;
import com.example.backend.domain.enums.Role;
import com.example.backend.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public UserInfo saveUser(RequestRegisterUser registerUser) {
        try {
            UserInfo userInfo = UserInfo.builder()
                    .username(registerUser.getUsername())
                    .email(registerUser.getEmail())
                    .password(passwordEncoder.encode(registerUser.getPassword()))
                    .userRole(Role.READ)
                    .build();

            return userInfoRepository.save(userInfo);
        }catch (Exception e){
            log.info("사용자 저장에 실패 했습니다. {}", e.getMessage());
            throw new IllegalStateException("사용자 저장에 실패 했습니다.");
        }
    }

    @Transactional
    public UserInfo changeUserState(String email){
        Optional<UserInfo> optional = userInfoRepository.findUserInfoByEmail(email);

        if(optional.isEmpty()){
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.");
        }

        UserInfo userInfo = optional.get();
        return userInfo.changeState(!userInfo.isState());
    }

    @Transactional
    public UserInfo changeUserRole(String email, Role role){
        Optional<UserInfo> optional = userInfoRepository.findUserInfoByEmail(email);

        if(optional.isEmpty()){
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.");
        }

        UserInfo userInfo = optional.get();
        return userInfo.changeRole(role);
    }
}
