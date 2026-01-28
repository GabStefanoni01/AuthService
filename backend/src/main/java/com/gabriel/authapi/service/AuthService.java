package com.gabriel.authapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabriel.authapi.domain.entity.RefreshToken;
import com.gabriel.authapi.domain.entity.User;
import com.gabriel.authapi.dto.LoginRequest;
import com.gabriel.authapi.dto.LoginResponse;
import com.gabriel.authapi.repository.RefreshTokenRepository;
import com.gabriel.authapi.repository.UserRepository;
import com.gabriel.authapi.security.jwt.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService,
            LoginAttemptService loginAttemptService
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        // Verifica se está bloqueado
        if (loginAttemptService.isBlocked(user)) {
            throw new RuntimeException(
                    "Account temporarily locked. Try later."
            );
        }

        // Senha inválida
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            loginAttemptService.loginFailed(user);

            throw new RuntimeException("Invalid credentials");
        }

        // Login OK
        loginAttemptService.loginSucceeded(user);

        // Revoga tokens antigos do usuário
        List<RefreshToken> tokens =
                refreshTokenRepository
                        .findByUserAndRevokedFalse(user);

        tokens.forEach(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });

        // Gera novos tokens
        String accessToken =
                jwtService.generateAccessToken(user.getEmail());

        String refreshTokenValue =
                jwtService.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );

        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(
                accessToken,
                refreshTokenValue
        );
    }
}
