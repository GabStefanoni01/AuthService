package com.gabriel.authapi.exception;

public class InvalidCredentialsException
        extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
