package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "o_transportation_provider_profile")
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Entity
public class TransportationProviderProfile extends BaseData {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    private String companyName;
    private String licNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private List<String> documents;
}
