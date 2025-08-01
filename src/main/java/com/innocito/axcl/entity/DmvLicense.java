package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "o_dmv_license")
public class DmvLicense extends BaseData {
    @Id
    private UUID id;
    @OneToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;
    @Column(name = "license_number")
    private String licenseNumber;
    @Column(name = "state_code")
    private String stateCode;
    @Column(name = "license_class")
    private String licenseClass;
    @Column(name = "document_url")
    private String documentUrl;
    @Column(name = "effective_date")
    private LocalDate effectiveDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    /*@Type(StringArrayType.class)
    @JdbcTypeCode(SqlTypes.ARRAY)*/
    @Column(name = "endorsements", columnDefinition = "text[]")
    private List<String> endorsements;
    /*@Type(StringArrayType.class)
    @JdbcTypeCode(SqlTypes.ARRAY)*/
    @Column(name = "restrictions", columnDefinition = "text[]")
    private List<String> restrictions;
}
