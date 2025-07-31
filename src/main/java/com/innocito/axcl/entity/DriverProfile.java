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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "transport_provider_id", referencedColumnName = "id")
    private TransportationProviderProfile transportProvider;
    @Column(name = "first_name")
    private String firstName;//mandatory
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;//mandatory
    @Column(name = "social_security_number")
    private String socialSecurityNumber;//mandatory
    private Integer gender;//mandatory
    private LocalDate dob;//mandatory
    @Column(name = "user_status")
    private Integer userStatus;//mandatory
    @Column(name = "phone_number")
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
    @Column(name = "zip_code")
    private String zipCode;
}
