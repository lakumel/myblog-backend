package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAuthenticatedUser extends RuntimeException{
    public NotAuthenticatedUser(String message){
        super(message);
    }
}
