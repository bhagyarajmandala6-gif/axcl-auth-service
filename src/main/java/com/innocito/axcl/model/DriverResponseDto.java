package com.innocito.axcl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDto {

    private UUID driverId;
    private String dob;
    private String ssnLast4;
    private String phoneNumber;
    private AddressDto address;
    private TransportProviderDto transportProvider;
    private Object dmvLicense; // use actual class if available
    private List<String> extraCommercialLicenses;
    private Object backgroundCheck;
}

