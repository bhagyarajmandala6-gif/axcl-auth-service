package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "o_extra_commercial_license")
public class ExtraCommercialLicense extends BaseData{
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "driverId")
    private DriverProfile driver;
    @Column
    private String licenseNumber;
    private Integer typeId;
    private String documentUrl;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
}
