package com.innocito.axcl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Entity
@Table(name = "o_users")
public class User extends BaseData {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String mobileNumber;
    private String iso2CountryCode;
    private String passwordHash;
    private Integer gender;
    private String profileImg;
    private Integer userRole;
    private Integer userStatus;
}