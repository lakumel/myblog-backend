package com.example.backend.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN"),
    WRITE("ROLE_WRITE"),
    READ("ROLE_READ");

    private final String role;
}
