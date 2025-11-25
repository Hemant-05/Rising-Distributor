package com.rising.Distributor.service;
import com.rising.Distributor.dto.LogInRequest;
import com.rising.Distributor.model.AppUser;
import com.rising.Distributor.repository.AppUserRepository;
import com.rising.Distributor.util.IdentifierUtils;
import com.rising.Distributor.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final SmsService smsService;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil = new JwtUtil();

    public AppUserService(AppUserRepository userRepository, SmsService smsService) {
        this.userRepository = userRepository;
        this.smsService = smsService;
    }

    public AppUser registerUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(false);
        return userRepository.save(user);
    }

    public void saveMobileNumber(String userId, String mobileNumber) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setNumber(mobileNumber);
            userRepository.save(user);
        });
    }

    public String generateOtp(String mobileNumber, boolean isFromReset) {
        // Generate random 6 digit OTP
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));
        String num = "+91" + mobileNumber;
        if(!isFromReset){
            otpStorage.put(num, otp);
            smsService.sendOtp(num,otp);
        }
        return otp;
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        mobileNumber = "+91" + mobileNumber;
        String storedOtp = otpStorage.get(mobileNumber);
        if (storedOtp != null && storedOtp.equals(otp)) {
            userRepository.findByNumber(mobileNumber).ifPresent(user -> {
                user.setIsVerified(true);
                userRepository.save(user);
            });
            otpStorage.remove(mobileNumber);
            return true;
        } else {
            return false;
        }
    }

    private final Map<String, String> resetOtpStorage = new HashMap<>(); // In-memory OTP store

    public boolean requestPasswordReset(String identifier) {
        Optional<AppUser> userOpt = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByNumber(identifier));

//        var x = IdentifierUtils.isEmail(identifier);

        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            String num = "+91"+user.getNumber();
            String otp = generateOtp(num,true);
            resetOtpStorage.put(identifier, otp);
            smsService.sendOtp(num, otp);

            // TODO: Send email OTP if needed

            return true;
        }
        return false;
    }

    public boolean verifyResetOtp(String identifier, String otp) {
        return resetOtpStorage.getOrDefault(identifier, "").equals(otp);
    }

    public boolean resetPassword(String identifier, String newPassword) {
        Optional<AppUser> userOpt = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByNumber(identifier));

        if (userOpt.isPresent() && verifyResetOtp(identifier, resetOtpStorage.get(identifier))) {
            AppUser user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword)); // BCrypt
            userRepository.save(user);
            resetOtpStorage.remove(identifier);
            return true;
        }
        return false;
    }
    public Optional<AppUser> findByIdentifier(String identifier)
    {
        String id = identifier == null ? "" : identifier.trim();
        if (IdentifierUtils.isEmail(id))
        {
            return userRepository.findByEmail(id);
        }
        String phone = IdentifierUtils.normalizePhone(id);
        if (phone != null) {
            return userRepository.findByNumber(phone);
        }
        return Optional.empty();
    }

    public Map<String, Object> login(LogInRequest loginRequest){
        Optional<AppUser> userOpt = userRepository.findByEmail(loginRequest.getIdentifier())
                .or(() -> userRepository.findByNumber(loginRequest.getIdentifier()));
        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            AppUser user = userOpt.get();
            String jwtToken = jwtUtil.generateToken(user.getUid(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("userId", user.getUid());
            response.put("role", user.getRole());
            response.put("message", "Login successful");

            return response;
        }
        Map<String,Object> res = new HashMap<>();
        res.put("userId",null);
        return res;
    }
}
