package com.innocito.axcl.repository;


import com.innocito.axcl.entity.RiderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RiderProfileRepository extends JpaRepository<RiderProfile, UUID> {
    RiderProfile findByUserId(UUID id);

}
