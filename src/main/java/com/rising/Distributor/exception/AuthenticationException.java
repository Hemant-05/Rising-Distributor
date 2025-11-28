package com.rising.Distributor.exception;

// The @ResponseStatus annotation has been removed to keep the design consistent
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
