package com.rising.Distributor.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_MINUTES = 5; // OTP valid for 5 minutes
    private final String accountSid;
    private final String authToken;
    private final String fromPhone;

   public OtpService(
            @Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken,
            @Value("${twilio.from-phone}") String fromPhone) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhone = fromPhone;
        Twilio.init(this.accountSid, this.authToken);
    }
    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public LocalDateTime generateOtpExpiryTime() {
        return LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES);
    }

    // In a real application, this would send an SMS
    public void sendOtp(String mobileNumber, String otp) {
        Message.creator(
                new PhoneNumber(mobileNumber),
                new PhoneNumber(fromPhone),
                "Your OTP code is: " + otp).create();
        }
}
