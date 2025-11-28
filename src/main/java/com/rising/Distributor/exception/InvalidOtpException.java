package com.rising.Distributor.exception;

// The @ResponseStatus annotation has been removed to keep the design consistent
public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
