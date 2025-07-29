package com.innocito.axcl.service;

import com.innocito.axcl.enums.GenderType;
import com.innocito.axcl.model.MasterDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MasterDataService {

    public MasterDataResponse getMasterData() {
        MasterDataResponse masterDataResponse = new MasterDataResponse();
        masterDataResponse.setGenders(GenderType.getKeys());
        return masterDataResponse;
    }
}
