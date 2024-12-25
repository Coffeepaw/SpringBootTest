package com.devsu.hackerearth.backend.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
public class PreconditionException extends Exception {
    public PreconditionException(String message) {
        super(message);
    }
}
