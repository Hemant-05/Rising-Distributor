package com.rising.Distributor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
