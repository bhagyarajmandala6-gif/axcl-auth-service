package com.innocito.axcl.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TenantData {
    private List<String> permissions = new ArrayList<>();
    private String loggedInUserId;
    private int loggedInUserType;
}