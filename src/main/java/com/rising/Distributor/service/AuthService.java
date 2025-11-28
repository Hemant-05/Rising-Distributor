package com.rising.Distributor.service;

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
    private final UserService userService;

    public AuthService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserService userService) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public String login(String emailOrMobile, String password) {
        if (emailOrMobile.contains("@")) {
            Optional<Admin> admin = adminRepository.findByEmail(emailOrMobile);
            if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
                return jwtUtil.generateToken(admin.get().getUid(), "ADMIN");
            }
        }

        Optional<User> user = userService.findByEmailOrMobile(emailOrMobile);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtil.generateToken(user.get().getUid(), "USER");
        }

        throw new AuthenticationException("Invalid email/mobile or password");
    }
}
