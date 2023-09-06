package com.example.backend.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class EncodedUserInfo {
    private final PasswordEncoder passwordEncoder;

    private String username;
    private String email;
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
