package com.rising.Distributor.exception;

import com.rising.Distributor.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // A single handler for all "Not Found" scenarios
    @ExceptionHandler({ResourceNotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(Exception ex) {
        String message = ex instanceof NoResourceFoundException ? "The requested API endpoint does not exist." : ex.getMessage();
        return ApiResponse.error(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOtpException(InvalidOtpException ex) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // Handle cases where the request body is missing or invalid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.error(HttpStatus.BAD_REQUEST, "Invalid request body: " + errorMessage);
    }

    // Generic exception handler for any other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        // Log the exception for debugging purposes
        ex.printStackTrace();
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
}
