
package com.innocito.axcl.config;

import com.innocito.axcl.entity.AuthToken;
import com.innocito.axcl.entity.User;
import com.innocito.axcl.enums.UserRole;
import com.innocito.axcl.model.AuthResponse;
import com.innocito.axcl.model.RefreshTokenRequest;
import com.innocito.axcl.model.RefreshTokenResponse;
import com.innocito.axcl.repository.AuthTokenRepository;
import com.innocito.axcl.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import static com.innocito.axcl.util.ApiDocConstants.PERMISSIONS;
import static com.innocito.axcl.util.ApplicationConstants.*;
import static com.innocito.axcl.util.PropertyNameConstants.*;

@Service
@RequiredArgsConstructor
public class JwtUtil {
    @Value(JWT_SIGNING_KEY)
    public String SIGNING_KEY;
    @Value(JWT_TOKEN_VALID_IN_MILLIS)
    public long TOKEN_VALIDITY_IN_MILLIS;
    @Value(JWT_RF_TOKEN_VALID_IN_DAYS)
    public long RF_TOKEN_VALID_IN_DAYS;
    @Value(SPRING_PROFILES_ACTIVE)
    private String activeProfile;

    private final AuthTokenRepository authTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsRevolver) {
        final Claims claims = extractAllClaims(token);
        return claimsRevolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(List<String> permissions,
                                String loggedInUserId, int loggedInUserType,
                                int customExpiryInMinutes) {
        Long expirationInMillis = null;
        if ((activeProfile.equalsIgnoreCase(LOCAL)
                || activeProfile.equalsIgnoreCase(DEV)
                || activeProfile.equalsIgnoreCase(STAGING))
                && customExpiryInMinutes > 0 && customExpiryInMinutes < 24 * 60) {
            expirationInMillis = customExpiryInMinutes * 60 * 1000L;
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(PERMISSIONS, permissions);
        claims.put(LOGGED_IN_USER_ID, loggedInUserId);
        claims.put(LOGGED_IN_USER_TYPE, loggedInUserType);
        return createToken(claims, loggedInUserId, expirationInMillis);
    }

    public String createToken(Map<String, Object> claims, String subject, Long expirationInMillis) {
        if (expirationInMillis == null) {
            expirationInMillis = TOKEN_VALIDITY_IN_MILLIS;
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public String generateRefreshToken(String userId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(RF_TOKEN_VALID_IN_DAYS))))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
        AuthToken authToken = new AuthToken();
        authToken.setRefreshToken(refreshToken);
        authToken.setUserId(userId);
        authToken.setExpiresAt(Date.from(Instant.now().plus(Duration.ofDays(RF_TOKEN_VALID_IN_DAYS))));
        authTokenRepository.save(authToken);
        return  refreshToken;
    }

    private String extractUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException ex) {
            throw new RuntimeException("Invalid refresh token", ex);
        }
    }

    public RefreshTokenResponse validateAndGenerateNewToken(RefreshTokenRequest request) {
        String incomingRefreshToken = request.getRefreshToken();

        String userIdFromToken = extractUserIdFromToken(incomingRefreshToken);

        AuthToken storedToken = authTokenRepository.findByRefreshToken(incomingRefreshToken);
        if (storedToken == null) {
            throw new RuntimeException("Refresh token not found");
        }

        if (!storedToken.getUserId().equals(userIdFromToken)) {
            throw new RuntimeException("Token subject does not match stored user");
        }

        if (storedToken.getExpiresAt().before(new Date())) {
            throw new RuntimeException("Refresh token expired");
        }

        User user = (User) customUserDetailsService.loadUserByUsername(userIdFromToken);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        UserRole role = UserRole.getByValue(user.getUserRole());

        List<String> permissions = (authorities == null) ? Collections.emptyList() :
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();
        String  accessToken = generateToken(permissions ,user.getEmail(), role.getValue(),0);

        return RefreshTokenResponse.builder()
                                 .accessToken(accessToken).build();
    }


}

