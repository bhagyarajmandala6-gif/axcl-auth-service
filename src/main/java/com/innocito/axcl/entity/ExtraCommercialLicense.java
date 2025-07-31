package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "o_extra_commercial_license")
public class ExtraCommercialLicense extends BaseData {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;
    @Column(name = "license_number")
    private String licenseNumber;
    @Column(name = "type_id")
    private Integer typeId;
    @Column(name = "document_url")
    private String documentUrl;
    @Column(name = "effective_date")
    private LocalDate effectiveDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}
