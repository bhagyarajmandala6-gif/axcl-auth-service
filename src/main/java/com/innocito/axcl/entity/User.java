package com.innocito.axcl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.stereotype.Indexed;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Entity
@Table(name = "o_users")
public class User extends BaseData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String mobileNumber;
    private String iso2CountryCode;
    private String password;
    private Integer gender;
    private String profileImg;
    private Integer userType;
    private Integer userStatus;
}