package com.innocito.axcl.model;


import lombok.*;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    private String client_id;
}
