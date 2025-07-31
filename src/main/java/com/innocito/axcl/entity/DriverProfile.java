package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Table(name = "o_driver_profile")
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Entity
public class DriverProfile extends BaseData {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "transportProviderId", referencedColumnName = "id")
    private TransportationProviderProfile transportProvider;

    private String firstName;//mandatory
    private String middleName;
    private String lastName;//mandatory
    private String socialSecurityNumber;//mandatory
    private Integer gender;//mandatory
    private LocalDate dob;//mandatory
    private Integer userStatus;//mandatory
    private String phoneNumber;//mandatory

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private DmvLicense dmvLicense;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<ExtraCommercialLicense> extraCommercialLicenses;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private BackgroundCheck backgroundCheck;

    // Address
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
