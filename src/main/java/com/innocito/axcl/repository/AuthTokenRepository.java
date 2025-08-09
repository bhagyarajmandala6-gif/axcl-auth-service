package com.innocito.axcl.repository;


import com.innocito.axcl.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    AuthToken findByRefreshToken(String refreshToken);

}
