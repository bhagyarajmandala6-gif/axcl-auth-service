package com.innocito.axcl.model;


import com.innocito.axcl.entity.DriverProfile;
import com.innocito.axcl.entity.RiderProfile;
import com.innocito.axcl.entity.TransportationProviderProfile;
import com.innocito.axcl.entity.User;
import com.innocito.axcl.enums.GenderType;
import com.innocito.axcl.enums.UserRole;
import com.innocito.axcl.enums.UserStatus;
import com.innocito.axcl.util.BasicUtils;

import java.util.Optional;

public class ResponseMapper {
    public static UserResponseDto mapUserToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(BasicUtils.maskEmail(user.getEmail()))
                .isoCountryCode(user.getIso2CountryCode())
                .mobileNumber(BasicUtils.maskMobileNumber(user.getMobileNumber()))
                .gender(Optional.ofNullable(user.getGender())
                        .map(GenderType::getByValue)
                        .map(Enum::toString)
                        .orElse(null))
                .userType(Optional.ofNullable(user.getUserStatus())
                        .map(UserStatus::getByValue)
                        .map(Enum::toString)
                        .orElse(null))
                .userRole(Optional.ofNullable(user.getUserRole())
                        .map(UserRole::getByValue)
                        .map(Enum::toString)
                        .orElse(null))
                .build();
    }

    public static DriverResponseDto mapDriverToDto(DriverProfile driver) {
        return DriverResponseDto.builder()
                .driverId(driver.getId())
                .ssnLast4(BasicUtils.maskSSN(driver.getSocialSecurityNumber()))
                .dob(driver.getDob().toString())
                .address(AddressDto.builder()
                        .street(driver.getAddress())
                        .city(driver.getCity())
                        .state(driver.getState())
                        .zipCode(driver.getZipCode())
                        .build())
                .transportProvider(TransportProviderDto.builder()
                        .transportProviderId(driver.getTransportProvider().getId())
                        .companyName(driver.getTransportProvider().getCompanyName())
                        .licenseNumber(driver.getTransportProvider().getLicNumber())
                        .address(AddressDto.builder()
                                .street(driver.getTransportProvider().getAddress())
                                .city(driver.getTransportProvider().getCity())
                                .state(driver.getTransportProvider().getState().trim())
                                .zipCode(driver.getTransportProvider().getZipCode())
                                .build())
                        .documents(driver.getTransportProvider().getDocuments())
                        .build())
                .build();
    }

    public static TransportProviderDto mapTransportDto(TransportationProviderProfile tp) {
        return TransportProviderDto.builder()
                .transportProviderId(tp.getId())
                .companyName(tp.getCompanyName())
                .licenseNumber(tp.getLicNumber())
                .address(AddressDto.builder()
                        .street(tp.getAddress())
                        .city(tp.getCity())
                        .state(tp.getState().trim())
                        .zipCode(tp.getZipCode())
                        .build())
                .build();
    }

    public static RiderResponseDto mapRiderToDto(RiderProfile rider) {
        if (rider == null) return null;

        return RiderResponseDto.builder()
                .riderId(rider.getId())
                .profilePicture(rider.getProfilePicture())
                .address(rider.getAddress())
                .city(rider.getCity())
                .state(rider.getState())
                .zipCode(rider.getZipCode())
                .dob(rider.getDob())
                .build();
    }


}
