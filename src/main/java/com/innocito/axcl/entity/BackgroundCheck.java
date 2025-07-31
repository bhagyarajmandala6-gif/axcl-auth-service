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
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;
    @Column(name = "document_url")
    private String documentUrl;
    @Column(name = "effective_date")
    private LocalDate effectiveDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}
