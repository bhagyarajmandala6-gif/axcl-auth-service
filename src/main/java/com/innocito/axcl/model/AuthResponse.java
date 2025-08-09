package com.innocito.axcl.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private UserResponseDto userInfo;
    private RiderResponseDto riderInfo;
    private TransportProviderDto transportProviderInfo;
    private DriverResponseDto driverInfo;
    private String accessToken;
    private String refreshToken;

}
