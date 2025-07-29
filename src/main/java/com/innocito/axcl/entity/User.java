package com.innocito.axcl.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Document(collection = "o_users")
public class User extends BaseData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String name;
    @Indexed
    private String email;
    @Indexed
    private String mobileNumber;
    private String iso2CountryCode;
    private String password;
    private Integer gender;
    private String profileImg;
    private Integer userType;
    private Integer userStatus;
}