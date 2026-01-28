package com.gabriel.authapi.repository;

import com.gabriel.authapi.domain.entity.RefreshToken;
import com.gabriel.authapi.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUserAndRevokedFalse(User user);

}
