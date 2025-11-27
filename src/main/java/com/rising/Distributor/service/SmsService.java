package com.rising.Distributor.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private final String accountSid;
    private final String authToken;
    private final String fromPhone;

    public SmsService(
            @Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken,
            @Value("${twilio.from-phone}") String fromPhone) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhone = fromPhone;
        Twilio.init(this.accountSid, this.authToken);
    }

    public void sendOtp(String toPhone, String otp) {
        Message.creator(
                new PhoneNumber(toPhone),
                new PhoneNumber(fromPhone),
                "Your OTP code is: " + otp).create();
    }
}