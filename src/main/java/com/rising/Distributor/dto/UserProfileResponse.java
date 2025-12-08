package com.rising.Distributor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileResponse {
    private String uid;
    private String name;
    private String email;
    private String mobileNumber;
    private Boolean isMobileVerified;
    private String role;
}
