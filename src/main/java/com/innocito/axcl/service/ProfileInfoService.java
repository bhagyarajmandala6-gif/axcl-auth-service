package com.innocito.axcl.service;

import com.innocito.axcl.entity.DriverProfile;
import com.innocito.axcl.entity.RiderProfile;
import com.innocito.axcl.entity.TransportationProviderProfile;
import com.innocito.axcl.repository.DriverProfileRepository;
import com.innocito.axcl.repository.RiderProfileRepository;
import com.innocito.axcl.repository.TransportProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileInfoService {

    private final DriverProfileRepository driverProfileRepo;
    private final RiderProfileRepository riderProfileRepo;
    private final TransportProviderProfileRepository transportProviderProfileRepo;

    public RiderProfile getRiderProfile(UUID  id) {
        return  riderProfileRepo.findByUserId(id);
    }
    public TransportationProviderProfile getTransportProviderProfile(UUID id) {
        return  transportProviderProfileRepo.findByUserId(id);
    }

    public DriverProfile getDriverProfile(UUID id)
    {
        return  driverProfileRepo.findByUserId(id);
    }


}
