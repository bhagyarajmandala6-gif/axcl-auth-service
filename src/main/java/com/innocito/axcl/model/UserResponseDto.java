package com.innocito.axcl.model;

import com.innocito.axcl.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String userType;
    private String gender;
    private String userRole;
    private String isoCountryCode;
    private String mobileNumber;
}
