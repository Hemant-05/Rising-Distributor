package com.rising.Distributor.exception;

// The @ResponseStatus annotation has been removed to resolve the ambiguity
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
