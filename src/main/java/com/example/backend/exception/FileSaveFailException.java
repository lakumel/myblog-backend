package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileSaveFailException extends RuntimeException{
    public FileSaveFailException(String message){
        super(message);
    }
}
