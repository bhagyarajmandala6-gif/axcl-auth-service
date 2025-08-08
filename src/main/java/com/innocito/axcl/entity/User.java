package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "o_users")
public class User extends BaseData {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String email;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "iso2_country_code")
    private String iso2CountryCode;
    @Column(name = "password_hash")
    private String passwordHash;
    private Integer gender;
    @Column(name = "profile_img")
    private String profileImg;
    @Column(name = "user_role")
    private Integer userRole;
    @Column(name = "user_status")
    private Integer userStatus;
    @Column(name = "last_logout_at")
    private Date lastLogoutAt;
}