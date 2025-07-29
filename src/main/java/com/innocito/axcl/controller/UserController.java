package com.innocito.axcl.controller;
/*
import com.innocito.axcl.annotations.Priority;
import com.innocito.axcl.model.*;
import com.innocito.axcl.util.BasicUtils;
import com.innocito.axcl.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import static com.innocito.axcl.util.ApiDocConstants.*;
import static com.innocito.axcl.util.ApplicationConstants.HIGH_PRIORITY;
import static com.innocito.axcl.util.ApplicationConstants.USER;

@Priority(HIGH_PRIORITY)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BasicUtils basicUtils;
    private final ResponseUtils responseUtils;

    @Operation(summary = API_TO_CREATE_USER, description = GENERAL_DESCRIPTION)
    @PostMapping("/registerOrLogin")
    public ResponseEntity<ApiResponse<AuthResponse>> registerOrLogin(@Valid @RequestBody UserRegistrationModel userRegistrationModel) throws IOException {
        AuthResponse authResponse = userService.registerOrLogin(userRegistrationModel);
        ApiResponse<AuthResponse> response = responseUtils.success(authResponse,
                basicUtils.getLocalizedMessage(USER_LOGGED_IN_SUCCESSFULLY, null));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_UPDATE_USER_PERSONAL_DETAILS, description = GENERAL_DESCRIPTION)
    @PutMapping("/personalInfo")
    public ResponseEntity<ApiResponse<PersonalDetailsResponseModel>> updatePersonalInfo(@Valid @RequestPart(USER_PERSONAL_INFO_REQUEST_MODEL) UserPersonalInfoRequestModel userPersonalInfoRequestModel,
                                                                                        @RequestPart(value = FILE, required = false) MultipartFile file) {
        PersonalDetailsResponseModel responseModel = userService.updatePersonalInfo(userPersonalInfoRequestModel, file);
        ApiResponse<PersonalDetailsResponseModel> response = responseUtils.success(responseModel,
                basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{PERSONAL_INFO, UPDATED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_FETCH_USER_PERSONAL_DETAILS, description = GENERAL_DESCRIPTION)
    @GetMapping("/personalInfo")
    public ResponseEntity<ApiResponse<PersonalDetailsResponseModel>> getPersonalInfo() {
        PersonalDetailsResponseModel responseModel = userService.getPersonalInfo();
        ApiResponse<PersonalDetailsResponseModel> response = responseUtils.success(responseModel, basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY, new Object[]{PERSONAL_INFO}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_UPDATE_USER_PROFILE_IMAGE, description = GENERAL_DESCRIPTION)
    @PutMapping(value = "/profileImage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ProfileImageResponse>> updateProfileImage(@RequestPart(FILE) MultipartFile file) {
        ProfileImageResponse result = userService.updateProfileImage(file);
        ApiResponse<ProfileImageResponse> response = responseUtils.success(result, basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{PROFILE_IMAGE, UPDATED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_DELETE_USER_PROFILE_IMAGE, description = GENERAL_DESCRIPTION)
    @DeleteMapping("/profileImage")
    public ResponseEntity<ApiResponse<Void>> deleteProfileImage() {
        userService.deleteProfileImage();
        ApiResponse<Void> response = responseUtils.success(null, basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{PROFILE_IMAGE, DELETED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_FETCH_USER_PROFILE_IMAGE, description = GENERAL_DESCRIPTION)
    @GetMapping("/profileImage")
    public ResponseEntity<ApiResponse<ProfileImageResponse>> getProfileImage() {
        ProfileImageResponse result = userService.getProfileImage();
        ApiResponse<ProfileImageResponse> response = responseUtils.success(result, basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY, new Object[]{PROFILE_IMAGE}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_UPDATE_USER_PERSONALITY_INFO, description = GENERAL_DESCRIPTION)
    @PutMapping("/personalityInfo")
    public ResponseEntity<ApiResponse<PersonalityInfoResponseModel>> updateUserPersonalityInfo(@Valid @RequestBody PersonalityInfoRequestModel personalityInfoRequestModel) {
        PersonalityInfoResponseModel result = userService.updateUserPersonalityInfo(personalityInfoRequestModel);
        ApiResponse<PersonalityInfoResponseModel> response = responseUtils.success(result, basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{PERSONALITY_INFO, UPDATED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_FETCH_USER_PERSONALITY_INFO, description = GENERAL_DESCRIPTION)
    @GetMapping("/personalityInfo")
    public ResponseEntity<ApiResponse<PersonalityInfoResponseModel>> getUserPersonalityInfo() {
        PersonalityInfoResponseModel result = userService.getUserPersonalityInfo();
        ApiResponse<PersonalityInfoResponseModel> response = responseUtils.success(result, basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY, new Object[]{PERSONALITY_INFO}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_USER_REFRESH_TOKEN_DATA, description = GENERAL_DESCRIPTION)
    @GetMapping("/accessToken")
    public ResponseEntity<ApiResponse<AuthResponse>> getUserAccessToken(@RequestParam String token) {
        AuthResponse result = userService.getUserAccessToken(token);
        ApiResponse<AuthResponse> response = responseUtils.success(result, basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY, new Object[]{DATA}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_USER_STYLE_REPORT_DATA, description = GENERAL_DESCRIPTION)
    @GetMapping("/styleReport/info")
    public ResponseEntity<ApiResponse<UserStyleReportBasicInfoModel>> getUserStyleReportInfo() {
        UserStyleReportBasicInfoModel result = userService.getUserStyleReportInfo();
        ApiResponse<UserStyleReportBasicInfoModel> response = responseUtils.success(result, basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY, new Object[]{DATA}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_SELECT_SUBSCRIPTION_PLANS, description = GENERAL_DESCRIPTION)
    @PostMapping("/subscriptionAddOn")
    public ResponseEntity<ApiResponse<Void>> subscriptionPlanAddOn(@RequestParam String subscriptionPlanId,
                                                                   @RequestParam SubscriptionType subscriptionType) {
        userService.subscriptionPlanAddOn(subscriptionPlanId, subscriptionType);
        ApiResponse<Void> response = responseUtils.success(null, basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{SUBSCRIPTION_PLAN, ADDED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_GET_USER_SUBSCRIBED_SUBSCRIPTION_PLAN_DETAILS, description = GENERAL_DESCRIPTION)
    @GetMapping("/subscriptionPlan/details")
    public ResponseEntity<ApiResponse<UserSubscriptionPlanResponseModel>> getUserSubscriptionPlanDetails() {
        UserSubscriptionPlanResponseModel surveyData = userService.getUserSubscriptionPlanDetails();
        ApiResponse<UserSubscriptionPlanResponseModel> response = responseUtils.success(surveyData,
                basicUtils.getLocalizedMessage(DATA_FETCH_SUCCESSFULLY, new Object[]{USER + DOUBLE_QUOTES_WITH_SINGLE_SPACE + SUBSCRIPTION_PLAN + DOUBLE_QUOTES_WITH_SINGLE_SPACE + DETAILS}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_GET_FACE_SHAPED_DETAILS, description = GENERAL_DESCRIPTION)
    @PutMapping(path = "/faceData", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<FaceDetails>> analyzeFaceImage(@RequestPart(FILE) MultipartFile file) {
        FaceDetails details = userService.analyzeFaceImage(file);
        ApiResponse<FaceDetails> response = responseUtils.success(details, basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{FACE_DATA, UPDATED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = API_TO_GET_BODY_SHAPE_DETAILS, description = GENERAL_DESCRIPTION)
    @PutMapping(path = "/bodyShapeData", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<BodyMeasurementsAnalysisModel>> analyzeBodyMeasurements(@RequestPart(FILE) MultipartFile file) {
        BodyMeasurementsAnalysisModel details = userService.analyzeBodyMeasurements(file);
        ApiResponse<BodyMeasurementsAnalysisModel> response = responseUtils.success(details, basicUtils.getLocalizedMessage(ACTION_PERFORMED_SUCCESSFULLY, new Object[]{BODY_MEASUREMENTS, UPDATED}));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}*/
