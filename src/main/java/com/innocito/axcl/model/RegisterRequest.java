package com.innocito.axcl.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private String iso2CountryCode;
    private Integer userStatus;
    private Integer userRole;
    private Integer gender;
}
