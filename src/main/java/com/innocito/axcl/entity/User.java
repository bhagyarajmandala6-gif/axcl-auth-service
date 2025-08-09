package com.innocito.axcl.entity;

import com.innocito.axcl.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "o_users")
public  class User extends BaseData implements UserDetails  {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(UserRole.getByValue(this.userRole).toString())
        );
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}