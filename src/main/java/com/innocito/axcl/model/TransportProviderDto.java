package com.innocito.axcl.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TransportProviderDto {
    private UUID transportProviderId;
    private String companyName;
    private String licenseNumber;
    private AddressDto address;
    private List<String> documents;
}
