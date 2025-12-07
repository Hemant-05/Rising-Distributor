package com.rising.Distributor.service;

import com.rising.Distributor.dto.AuthResponse;
import com.rising.Distributor.exception.AuthenticationException;
import com.rising.Distributor.model.Admin;
import com.rising.Distributor.model.User;
import com.rising.Distributor.repository.AdminRepository;
import com.rising.Distributor.repository.UserRepository;
import com.rising.Distributor.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse generateToken(String userId, String role){
        String accessToken = jwtUtil.generateAccessToken(userId, role);
        String refreshToken = jwtUtil.generateRefreshToken(userId);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(String emailOrMobile, String password) {
        if (emailOrMobile.contains("@")) {
            Optional<Admin> adminOpt = adminRepository.findByEmail(emailOrMobile);
            if (adminOpt.isPresent() && passwordEncoder.matches(password, adminOpt.get().getPassword())) {
                Admin admin = adminOpt.get();
                String accessToken = jwtUtil.generateAccessToken(admin.getUid(), "ADMIN");
                String refreshToken = jwtUtil.generateRefreshToken(admin.getUid());
                return new AuthResponse(accessToken, refreshToken);
            }
        }

        Optional<User> userOpt = userRepository.findByEmail(emailOrMobile).or(() -> userRepository.findByMobileNumber(emailOrMobile));
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            String accessToken = jwtUtil.generateAccessToken(user.getUid(), "USER");
            String refreshToken = jwtUtil.generateRefreshToken(user.getUid());
            return new AuthResponse(accessToken, refreshToken);
        }

        throw new AuthenticationException("Invalid email/mobile or password");
    }

    public AuthResponse refreshToken(String refreshToken) {
        try {
            String userId = jwtUtil.extractUsername(refreshToken);
            
            // Check if the user is an admin or a regular user
            Optional<Admin> adminOpt = adminRepository.findById(userId);
            if (adminOpt.isPresent()) {
                String newAccessToken = jwtUtil.generateAccessToken(userId, "ADMIN");
                return new AuthResponse(newAccessToken, refreshToken);
            }

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                String newAccessToken = jwtUtil.generateAccessToken(userId, "USER");
                return new AuthResponse(newAccessToken, refreshToken);
            }
            
            throw new AuthenticationException("Invalid refresh token: User not found");

        } catch (Exception e) {
            throw new AuthenticationException("Invalid or expired refresh token");
        }
    }
}
