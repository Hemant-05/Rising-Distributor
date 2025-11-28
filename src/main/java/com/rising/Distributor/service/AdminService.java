package com.rising.Distributor.service;

import com.rising.Distributor.model.Admin;
import com.rising.Distributor.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin registerAdmin(String name, String email, String password) {
        Admin newAdmin = new Admin(name, email, passwordEncoder.encode(password), "ADMIN");
        return adminRepository.save(newAdmin);
    }
}
