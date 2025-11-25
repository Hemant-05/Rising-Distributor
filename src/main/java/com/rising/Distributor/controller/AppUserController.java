package com.rising.Distributor.controller;
import com.rising.Distributor.dto.LogInRequest;
import com.rising.Distributor.model.AppUser;
import com.rising.Distributor.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@Validated @RequestBody AppUser user) {
        // Add password confirmation check on frontend or validation layer
        AppUser createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/{userId}/mobile")
    public ResponseEntity<String> addMobileNumber(@PathVariable String userId, @RequestBody String mobileNumber) {
        userService.saveMobileNumber(userId, mobileNumber);
        String otp = userService.generateOtp(mobileNumber,false);
        return ResponseEntity.ok("OTP sent: " + otp); // In real app, do not send OTP in response
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        boolean isVerified = userService.verifyOtp(mobileNumber, otp);
        if (isVerified) {
            return ResponseEntity.ok("User verified successfully");
        } else {
            return ResponseEntity.status(400).body("OTP verification failed");
        }
    }

    @PostMapping("/forgot-password/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String identifier) {
        // identifier can be email or mobile number
        boolean requestSent = userService.requestPasswordReset(identifier);
        return requestSent ?
                ResponseEntity.ok("OTP sent successfully") :
                ResponseEntity.badRequest().body("User not found");
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<String> verifyResetOtp(@RequestParam String identifier, @RequestParam String otp) {
        boolean isValid = userService.verifyResetOtp(identifier, otp);
        return isValid ?
                ResponseEntity.ok("OTP verified. Proceed to reset password") :
                ResponseEntity.badRequest().body("Invalid OTP");
    }

    @PutMapping("/forgot-password/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String identifier,
                                                @RequestParam String newPassword) {
        boolean success = userService.resetPassword(identifier, newPassword);
        return success ?
                ResponseEntity.ok("Password reset successfully") :
                ResponseEntity.badRequest().body("Password reset failed");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Validated LogInRequest loginRequest) {
        try{
            var response = userService.login(loginRequest);
            if(response.get("userId") == null || Objects.equals(response.get("userId"), "")){;
                return ResponseEntity.status(401).body(new HashMap<>() {{
                    put("error", "Invalid credentials");
                }});
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(new HashMap<>() {{
                put("error", e.getMessage());
            }});
        }
    }


}
