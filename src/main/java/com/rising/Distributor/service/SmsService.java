package com.rising.Distributor.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private final String ACCOUNT_SID = "AC379c22082ca3fa8f1b8a1a43f4b414a3";
    private final String AUTH_TOKEN = "0ccd7b0fbde00e4497f035b3a257ec04";
    private final String FROM_PHONE = "+13018446159";

    public SmsService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendOtp(String toPhone, String otp) {
        Message.creator(
                new com.twilio.type.PhoneNumber(toPhone),
                new com.twilio.type.PhoneNumber(FROM_PHONE),
                "Your OTP code is: " + otp).create();
    }
}