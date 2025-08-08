package com.innocito.axcl.repository;

import com.innocito.axcl.entity.TransportationProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportProviderRepository extends JpaRepository<TransportationProviderProfile, UUID> {
    Optional<TransportationProviderProfile> findByCompanyName(String name);
}