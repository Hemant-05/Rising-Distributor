package com.rising.Distributor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetVerificationRequest {
    private String emailOrMobile;
    private String otp;
    private String newPassword;
}
