package com.rising.Distributor.service;

import com.rising.Distributor.exception.InvalidOtpException;
import com.rising.Distributor.exception.ResourceNotFoundException;
import com.rising.Distributor.model.User;
import com.rising.Distributor.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    public User registerUser(String name, String email, String mobileNumber, String password) {
        User newUser = new User(name, email, mobileNumber, passwordEncoder.encode(password));
        return userRepository.save(newUser);
    }

    public void saveMobileNumber(String userId, String mobileNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setMobileNumber(mobileNumber);
        userRepository.save(user);
        generateAndSendOtp(user);
    }

    public void generateAndSendOtp(User user) {
        String otp = otpService.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryTime(otpService.generateOtpExpiryTime());
        userRepository.save(user);
        otpService.sendOtp(user.getMobileNumber(), otp);
    }

    public void verifyMobileOtp(String userId, String otp) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp) ||
            user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP is invalid or has expired");
        }
        
        user.setIsMobileVerified(true);
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);
    }

    public void requestPasswordReset(String emailOrMobile) {
        User user = findByEmailOrMobile(emailOrMobile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with provided email or mobile number"));
        generateAndSendOtp(user);
    }
    
    public void verifyPasswordResetOtpAndReset(String emailOrMobile, String otp, String newPassword) {
        User user = findByEmailOrMobile(emailOrMobile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp) ||
            user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP is invalid or has expired");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);
    }
    
    public Optional<User> findByEmailOrMobile(String emailOrMobile) {
        if (emailOrMobile.contains("@")) {
            return userRepository.findByEmail(emailOrMobile);
        } else {
            return userRepository.findByMobileNumber(emailOrMobile);
        }
    }
}
