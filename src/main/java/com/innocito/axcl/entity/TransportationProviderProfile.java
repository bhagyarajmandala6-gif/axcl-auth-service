package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "lic_number")
    private String licNumber;
    private String address;
    private String city;
    private String state;
    @Column(name = "zip_code")
    private String zipCode;
    /*@Type(ListArrayType.class)*/
    @Column(name = "documents", columnDefinition = "text[]")
    private List<String> documents;
}
