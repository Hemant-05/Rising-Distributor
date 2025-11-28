package com.rising.Distributor.controller;

import com.rising.Distributor.dto.*;
import com.rising.Distributor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/mobile")
    public ResponseEntity<ApiResponse<Void>> saveMobileNumber(@RequestBody MobileRequest request, Authentication authentication) {
        String userId = authentication.getName();
        userService.saveMobileNumber(userId, request.getMobileNumber());
        return ApiResponse.success(HttpStatus.OK, "OTP sent to your mobile number for verification.");
    }

    @PostMapping("/mobile/verify")
    public ResponseEntity<ApiResponse<Void>> verifyMobile(@RequestBody OtpVerificationRequest request, Authentication authentication) {
        String userId = authentication.getName();
        userService.verifyMobileOtp(userId, request.getOtp());
        return ApiResponse.success(HttpStatus.OK, "Mobile number verified successfully.");
    }

    @PostMapping("/password/request-reset")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        userService.requestPasswordReset(request.getEmailOrMobile());
        return ApiResponse.success(HttpStatus.OK, "OTP sent for password reset.");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody PasswordResetVerificationRequest request) {
        userService.verifyPasswordResetOtpAndReset(request.getEmailOrMobile(), request.getOtp(), request.getNewPassword());
        return ApiResponse.success(HttpStatus.OK, "Password has been reset successfully.");
    }
}
