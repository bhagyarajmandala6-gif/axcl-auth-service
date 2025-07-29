/*
package com.innocito.axcl.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.innocito.axcl.util.ApplicationConstants.*;
import static com.innocito.axcl.util.MessageConstants.GENDER;
import static com.innocito.axcl.util.PropertyNameConstants.*;

@Service
public class JwtUtil {
    @Value(JWT_SIGNING_KEY)
    public String SIGNING_KEY;
    @Value(JWT_TOKEN_VALID_IN_MILLIS)
    public long TOKEN_VALIDITY_IN_MILLIS;
    @Value(SPRING_PROFILES_ACTIVE)
    private String activeProfile;

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
                                int customExpiryInMinutes, Integer gender) {
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
        claims.put(GENDER, gender);
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
}
*/
