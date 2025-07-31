package com.innocito.axcl.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rider_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderProfile {

    @Id
    private UUID id;

    private UUID userId;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private LocalDate dob;

    private String gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private String createdBy;

    private String updatedBy;

    private String deletedBy;

}
