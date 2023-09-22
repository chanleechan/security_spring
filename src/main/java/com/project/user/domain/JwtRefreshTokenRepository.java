package com.project.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    JwtRefreshToken findByRefreshToken(String keyId);

    Optional<JwtRefreshToken> findByKeyId(String keyId);
}
