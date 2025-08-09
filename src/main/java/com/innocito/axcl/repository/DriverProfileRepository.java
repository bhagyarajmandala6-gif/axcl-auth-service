package com.innocito.axcl.repository;


import com.innocito.axcl.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID> {
        DriverProfile findByUserId(UUID id);
}