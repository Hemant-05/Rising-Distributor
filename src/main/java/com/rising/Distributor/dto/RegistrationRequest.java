package com.rising.Distributor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String name;
    private String email;
    private String number;
    private String password;
}
