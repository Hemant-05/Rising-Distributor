package com.rising.Distributor.controller;

import com.rising.Distributor.dto.ApiResponse;
import com.rising.Distributor.dto.AuthResponse;
import com.rising.Distributor.dto.LogInRequest;
import com.rising.Distributor.dto.RegistrationRequest;
import com.rising.Distributor.service.AdminService;
import com.rising.Distributor.service.AuthService;
import com.rising.Distributor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AdminService adminService;
    private final AuthService authService;

    public AuthController(UserService userService, AdminService adminService, AuthService authService) {
        this.userService = userService;
        this.adminService = adminService;
        this.authService = authService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody RegistrationRequest request) {
        userService.registerUser(request.getName(), request.getEmail(), request.getNumber(), request.getPassword());
        return ApiResponse.success(HttpStatus.CREATED, "User registered successfully");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<Void>> registerAdmin(@RequestBody RegistrationRequest request) {
        adminService.registerAdmin(request.getName(), request.getEmail(), request.getPassword());
        return ApiResponse.success(HttpStatus.CREATED, "Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LogInRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        AuthResponse authResponse = new AuthResponse(token);
        return ApiResponse.success(HttpStatus.OK, "Login successful", authResponse);
    }
}
