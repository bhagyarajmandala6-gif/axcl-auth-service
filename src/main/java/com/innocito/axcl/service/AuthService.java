package com.innocito.axcl.service;

import com.innocito.axcl.config.JwtUtil;
import com.innocito.axcl.entity.User;
import com.innocito.axcl.enums.UserRole;
import com.innocito.axcl.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final ProfileInfoService profileInfoService;
    private final LogOutService logOutService;


    public ApiResponse<AuthResponse> login(LoginRequest loginRequest) {
        // 1. Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2. Load user details
        User user = (User) userDetailsService.loadUserByUsername(loginRequest.getEmail());
        UserRole role = UserRole.getByValue(user.getUserRole());

        // 3. Extract permissions
        List<String> permissions = Optional.ofNullable(user.getAuthorities())
                .orElse(Collections.emptyList())
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 4. Generate tokens
        String accessToken = jwtUtil.generateToken(permissions, user.getEmail(), role.getValue(), 0);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // 5. Build AuthResponse
        AuthResponse.AuthResponseBuilder builder = AuthResponse.builder()
                .userInfo(ResponseMapper.mapUserToDto(user))
                .accessToken(accessToken)
                .refreshToken(refreshToken);

        switch (role) {
            case RIDER -> builder.riderInfo(
                    ResponseMapper.mapRiderToDto(profileInfoService.getRiderProfile(user.getId()))
            );
            case TRANSPORT_PROVIDER -> builder.transportProviderInfo(
                    ResponseMapper.mapTransportDto(profileInfoService.getTransportProviderProfile(user.getId()))
            );
            case DRIVER -> builder.driverInfo(
                    ResponseMapper.mapDriverToDto(profileInfoService.getDriverProfile(user.getId()))
            );
            default -> throw new IllegalArgumentException("Unsupported role: " + user.getUserRole());
        }

        AuthResponse authResponse = builder.build();

        // 6. Wrap in API response and return
        return ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .data(authResponse)
                .build();
    }

    public ApiResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        if(request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
            RefreshTokenResponse refreshTokenResponse = jwtUtil.validateAndGenerateNewToken(request);

        return ApiResponse.<RefreshTokenResponse>builder()
                .message("Refresh Token Generated successful")
                .data(refreshTokenResponse)
                .build();
    }

    public ApiResponse<LogOutResponse> logout(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Authentication authentication) {

       logOutService.logout(request,response,authentication);



        return ApiResponse.<LogOutResponse>builder()
                .message("Logout successful")
                .build();
    }

    }
