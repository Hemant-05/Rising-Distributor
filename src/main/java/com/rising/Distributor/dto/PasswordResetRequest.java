package com.rising.Distributor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    private String emailOrMobile;
}
