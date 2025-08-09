package com.innocito.axcl.service;

import com.innocito.axcl.config.JwtUtil;
import com.innocito.axcl.entity.User;
import com.innocito.axcl.repository.AuthTokenRepository;
import com.innocito.axcl.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogOutService implements LogoutHandler {

    private final AuthTokenRepository refreshTokenRepo;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token is required");
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtUtil.extractUsername(jwt);

        User user = userRepository.findByEmail(userEmail);
        user.setLastLogoutAt(new Date());
        userRepository.save(user);
        SecurityContextHolder.clearContext();
    }

}
