package com.rising.Distributor.controller;

import com.rising.Distributor.dto.*;
import com.rising.Distributor.model.Admin;
import com.rising.Distributor.model.User;
import com.rising.Distributor.service.AdminService;
import com.rising.Distributor.service.AuthService;
import com.rising.Distributor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<ApiResponse<Object>> registerUser(@RequestBody RegistrationRequest request) {
        try{
            User user =  userService.registerUser(request.getName(), request.getEmail(), request.getNumber(), request.getPassword());
            AuthResponse tokens = authService.generateToken(user.getUid(), "USER");
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("tokens", tokens);
            return ApiResponse.success(HttpStatus.CREATED, "User registered successfully", data);
        }catch(Exception e) {
            return ApiResponse.error(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<Object>> registerAdmin(@RequestBody RegistrationRequest request) {
        try{
            Admin admin = adminService.registerAdmin(request.getName(), request.getEmail(), request.getPassword());
            AuthResponse tokens = authService.generateToken(admin.getUid(), "ADMIN");
            Map<String, Object> data = new HashMap<>();
            data.put("admin", admin);
            data.put("tokens", tokens);
            return ApiResponse.success(HttpStatus.CREATED, "Admin registered successfully", data);
        }catch(Exception e){
            return ApiResponse.error(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LogInRequest request) {
        AuthResponse tokens = authService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success(HttpStatus.OK, "Login successful", tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse newTokens = authService.refreshToken(request.getRefreshToken());
        return ApiResponse.success(HttpStatus.OK, "Token refreshed successfully", newTokens);
    }
}
