package com.innocito.axcl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderResponseDto {

    private UUID riderId;

    private String profilePicture;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private LocalDate dob;
}