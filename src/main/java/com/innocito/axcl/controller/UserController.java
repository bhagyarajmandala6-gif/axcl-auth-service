package com.innocito.axcl.controller;

import com.innocito.axcl.config.JwtUtil;
import com.innocito.axcl.model.*;

import com.innocito.axcl.repository.UserRepository;
import com.innocito.axcl.service.AuthService;
import com.innocito.axcl.service.CustomUserDetailsService;
import com.innocito.axcl.service.ProfileInfoService;
import com.innocito.axcl.util.ApiEndPoints;
import com.innocito.axcl.util.BasicUtils;
import com.innocito.axcl.util.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(ApiEndPoints.AUTHORIZATION)
@RequiredArgsConstructor
public class UserController {
//    private final BasicUtils basicUtils;
//    private final ResponseUtils responseUtils;
//
//    private final AuthenticationManager authenticationManager;
//
//    private final CustomUserDetailsService userDetailsService;
//
//
//
//    private final ProfileInfoService profileInfoService;
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
    private final AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
//        }
//
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setMobileNumber(request.getMobileNumber());
//        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
//        user.setUserRole(1);
//        user.setUserStatus(1); // default 1
//        user.setIso2CountryCode(request.getIso2CountryCode());
//        user.setCreatedOn(new Date());
//        user.setGender(1);
//        userRepository.save(user);
//
//        //userRepository.save(user);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
//    }
    @PostMapping(value = ApiEndPoints.LOGIN)
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthResponse> authResponse =  authService.login(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping(value = ApiEndPoints.REFRESH)
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        ApiResponse<RefreshTokenResponse> refreshTokenResponseApiResponse = authService.refreshToken(request);
        return new ResponseEntity<>(refreshTokenResponseApiResponse, HttpStatus.OK);

    }

    @PostMapping(value = ApiEndPoints.LOGOUT)
    public ResponseEntity<ApiResponse<LogOutResponse>> logout(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              Authentication authentication) {

        ApiResponse<LogOutResponse> logOutResponseApiResponse = authService.logout(request, response, authentication);
        return new ResponseEntity<>(logOutResponseApiResponse,HttpStatus.OK);
    }

}
