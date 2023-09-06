package com.example.backend.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestRegisterUser {

    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
