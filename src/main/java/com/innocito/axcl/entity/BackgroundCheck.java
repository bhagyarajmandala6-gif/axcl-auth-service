package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "o_background_check")
public class BackgroundCheck extends BaseData {
    @Id
    private UUID id;
    @OneToOne
    @JoinColumn(name = "driverId")
    private DriverProfile driver;
    private String documentUrl;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
}
