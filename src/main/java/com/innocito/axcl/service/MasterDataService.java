package com.innocito.axcl.service;

import com.innocito.axcl.entity.TransportationProviderProfile;
import com.innocito.axcl.entity.User;
import com.innocito.axcl.enums.GenderType;
import com.innocito.axcl.enums.UserRole;
import com.innocito.axcl.enums.UserStatus;
import com.innocito.axcl.model.MasterDataResponse;
import com.innocito.axcl.repository.TransportProviderRepository;
import com.innocito.axcl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MasterDataService {
    private final TransportProviderRepository transportProviderRepository;
    private final UserRepository userRepository;

    public MasterDataResponse getMasterData() {
        log.info("Fetching master data");
        MasterDataResponse masterDataResponse = new MasterDataResponse();
        masterDataResponse.setGenders(GenderType.getKeys());

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setMobileNumber("1234567890");
        user.setIso2CountryCode("US");
        user.setPasswordHash("hashed_password");
        user.setGender(GenderType.M.getValue()); // or use GenderType if applicable
        user.setProfileImg("profile.jpg");
        user.setUserRole(UserRole.TRANSPORT_PROVIDER.getValue());
        user.setUserStatus(UserStatus.ACTIVE.getValue());
        user.setLastLogoutAt(new Date());
        userRepository.save(user);

        TransportationProviderProfile transportationProviderProfile = new TransportationProviderProfile();
        transportationProviderProfile.setUser(user);
        transportationProviderProfile.setCompanyName("Default Provider");
        transportationProviderProfile.setLicNumber("LIC123456");
        transportationProviderProfile.setDocuments(List.of("abcd.pdf", "xyz.jpg"));

        transportProviderRepository.save(transportationProviderProfile);

        transportProviderRepository.findAll().forEach(provider -> {
            log.info("Transport Provider: {}, {}", provider.getCompanyName(),
                    provider.getDocuments());
        });


        return masterDataResponse;
    }
}
