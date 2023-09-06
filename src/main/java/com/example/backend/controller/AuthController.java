package com.example.backend.controller;

import com.example.backend.controller.dto.RequestRegisterUser;
import com.example.backend.domain.UserInfo;
import com.example.backend.domain.enums.Role;
import com.example.backend.security.service.UserInfoUserDetailsService;
import com.example.backend.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoService userInfoService;

    private final MessageSource messageSource;

    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody RequestRegisterUser registerUser) {
        registerUser.setPassword(registerUser.getPassword());
        UserInfo userinfo = userInfoService.saveUser(registerUser);

        return new ResponseEntity(messageSource.getMessage("created.message", new String[]{"User Account"}, null), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public void logout() {
    }
}
