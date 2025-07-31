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
    @JoinColumn(name = "transport_provider_id", referencedColumnName = "id")
    private TransportationProviderProfile transportProvider;
    private String vin;
    @Column(name = "fleet_number")
    private String fleetNumber;
    @Column(name = "production_year")
    private Integer productionYear;
    @Column(name = "type_id")
    private Integer typeId;
    private String make;
    private String model;
    private String color;
    @Column(name = "seat_number")
    private Integer seatNumber;
    @Column(name = "status_id")
    private Integer statusId;

    // DMV Registration
    @Column(name = "dmv_license_plate_number")
    private String dmvLicensePlateNumber;
    @Column(name = "dmv_license_plate_category_id")
    private Integer dmvLicensePlateCategoryId;
    @Column(name = "dmv_state_code")
    private String dmvStateCode;
    @Column(name = "dmv_effective_date")
    private LocalDate dmvEffectiveDate;
    @Column(name = "dmv_expiration_date")
    private LocalDate dmvExpirationDate;
    @Column(name = "dmv_document_url")
    private String dmvDocumentUrl;

    // Extra Commercial License
    @Column(name = "extra_license_number")
    private String extraLicenseNumber;
    @Column(name = "extra_type_id")
    private Integer extraTypeId;
    @Column(name = "extra_effective_date")
    private LocalDate extraEffectiveDate;
    @Column(name = "extra_expiration_date")
    private LocalDate extraExpirationDate;
    @Column(name = "extra_document_url")
    private String extraDocumentUrl;

    // Insurance
    @Column(name = "insurance_policy_number")
    private String insurancePolicyNumber;
    @Column(name = "insurance_insurer_name")
    private String insuranceInsurerName;
    @Column(name = "insurance_effective_date")
    private LocalDate insuranceEffectiveDate;
    @Column(name = "insurance_expiration_date")
    private LocalDate insuranceExpirationDate;
    @Column(name = "insurance_document_url")
    private String insuranceDocumentUrl;

    // Inspection
    @Column(name = "inspection_effective_date")
    private LocalDate inspectionEffectiveDate;
    @Column(name = "inspection_expiration_date")
    private LocalDate inspectionExpirationDate;
    @Column(name = "inspection_document_url")
    private String inspectionDocumentUrl;
}
