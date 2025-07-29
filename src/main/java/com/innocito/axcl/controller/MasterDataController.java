package com.innocito.axcl.controller;

import com.innocito.axcl.model.ApiResponse;
import com.innocito.axcl.model.MasterDataResponse;
import com.innocito.axcl.service.MasterDataService;
import com.innocito.axcl.util.ApiDocConstants;
import com.innocito.axcl.util.BasicUtils;
import com.innocito.axcl.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.innocito.axcl.util.ApiDocConstants.API_TO_FETCH_MASTER_DATA;
import static com.innocito.axcl.util.MessageConstants.DATA_FETCH_SUCCESSFULLY;
import static com.innocito.axcl.util.MessageConstants.MASTER_DATA;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/masterData")
public class MasterDataController {
    private final BasicUtils basicUtils;
    private final MasterDataService masterDataService;
    private final ResponseUtils responseUtils;

    @Operation(summary = API_TO_FETCH_MASTER_DATA,
            description = ApiDocConstants.GENERAL_DESCRIPTION)
    @GetMapping
    public ResponseEntity<ApiResponse<MasterDataResponse>> getMasterData() {
        MasterDataResponse responseModel = masterDataService.getMasterData();
        ApiResponse<MasterDataResponse> response = responseUtils
                .success(responseModel,
                        basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY,
                                new Object[]{MASTER_DATA}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
