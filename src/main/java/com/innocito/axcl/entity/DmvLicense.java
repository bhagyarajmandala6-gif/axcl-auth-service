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
    @JoinColumn(name = "driverId")
    private DriverProfile driver;
    @Column
    private String licenseNumber;
    @Column
    private String stateCode;
    @Column
    private String licenseClass;
    private String documentUrl;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private List<String> endorsements;
    private List<String> restrictions;
}
