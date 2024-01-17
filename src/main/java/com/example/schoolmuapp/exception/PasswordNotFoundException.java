package com.example.schoolmuapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
        (code = HttpStatus.BAD_REQUEST)
public class PasswordNotFoundException extends RuntimeException {
    public PasswordNotFoundException(String message) {
        super(message);
    }
}
