package com.innocito.axcl.repository;



import com.innocito.axcl.entity.TransportationProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransportProviderProfileRepository extends JpaRepository<TransportationProviderProfile, UUID> {
    TransportationProviderProfile findByUserId(UUID id);
}