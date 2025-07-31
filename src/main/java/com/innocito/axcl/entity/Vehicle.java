package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "o_vehicle")
@Data
public class Vehicle extends BaseData {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "transportProviderId", referencedColumnName = "id")
    private TransportationProviderProfile transportProvider;
    private String vin;
    private String fleetNumber;
    private Integer productionYear;
    private Integer typeId;
    private String make;
    private String model;
    private String color;
    private Integer seatNumber;
    private Integer statusId;

    // DMV Registration
    private String dmvLicensePlateNumber;
    private Integer dmvLicensePlateCategoryId;
    private String dmvStateCode;
    private LocalDate dmvEffectiveDate;
    private LocalDate dmvExpirationDate;
    private String dmvDocumentUrl;

    // Extra Commercial License
    private String extraLicenseNumber;
    private Integer extraTypeId;
    private LocalDate extraEffectiveDate;
    private LocalDate extraExpirationDate;
    private String extraDocumentUrl;

    // Insurance
    private String insurancePolicyNumber;
    private String insuranceInsurerName;
    private LocalDate insuranceEffectiveDate;
    private LocalDate insuranceExpirationDate;
    private String insuranceDocumentUrl;

    // Inspection
    private LocalDate inspectionEffectiveDate;
    private LocalDate inspectionExpirationDate;
    private String inspectionDocumentUrl;
}
