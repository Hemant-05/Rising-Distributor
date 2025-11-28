package com.rising.Distributor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't include null fields in the JSON response
public class ApiResponse<T> {

    private int statusCode;
    private String message;
    private T data;

    // Constructor for success responses
    public ApiResponse(HttpStatus status, String message, T data) {
        this.statusCode = status.value();
        this.message = message;
        this.data = data;
    }

    // Constructor for error responses (without data)
    public ApiResponse(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.message = message;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, message));
    }
}
