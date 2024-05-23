package com.example.bank.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserNotFoundException extends IllegalArgumentException {
    private final String message;

    public UserNotFoundException(Long userId) {
        this.message = "User with id " + userId + " does not exist.";
        log.warn(this.message);
    }
}
