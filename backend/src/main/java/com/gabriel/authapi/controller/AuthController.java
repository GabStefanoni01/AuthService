package com.gabriel.authapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gabriel.authapi.dto.*;
import com.gabriel.authapi.domain.entity.RefreshToken;
import com.gabriel.authapi.domain.entity.User;
import com.gabriel.authapi.security.jwt.JwtService;
import com.gabriel.authapi.service.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordRecoveryService passwordRecoveryService;

    public AuthController(
            UserService userService,
            AuthService authService,
            RefreshTokenService refreshTokenService,
            JwtService jwtService,
            PasswordRecoveryService passwordRecoveryService
    ) {

        this.userService = userService;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.passwordRecoveryService = passwordRecoveryService;
    }

    // ================= REGISTER =================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // ================= LOGOUT =================

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request) {

        refreshTokenService.revokeByToken(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

    // ================= REFRESH =================

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.validate(request.getRefreshToken());

        User user = refreshToken.getUser();

        // Revoga token atual
        refreshTokenService.revoke(refreshToken);

        String newAccessToken =
                jwtService.generateAccessToken(user.getEmail());

        String newRefreshToken =
                jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.create(user, newRefreshToken);

        return ResponseEntity.ok(
                new LoginResponse(newAccessToken, newRefreshToken)
        );
    }

    // ================= PASSWORD RECOVERY =================

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        passwordRecoveryService.forgotPassword(request.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        passwordRecoveryService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok().build();
    }
}
