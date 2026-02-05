package com.gabriel.authapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gabriel.authapi.dto.*;
import com.gabriel.authapi.domain.entity.RefreshToken;
import com.gabriel.authapi.domain.entity.User;
import com.gabriel.authapi.security.jwt.JwtService;
import com.gabriel.authapi.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(
    name = "Authentication",
    description = "Endpoints de autenticação, sessão e recuperação de senha"
)
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


    @Operation(
        summary = "Registrar usuário",
        description = "Cria uma nova conta de usuário"
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
        summary = "Login",
        description = "Autentica o usuário e retorna access e refresh token"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }


    @Operation(
        summary = "Logout",
        description = "Revoga o refresh token e encerra a sessão"
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request) {

        refreshTokenService.revokeByToken(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }


    @Operation(
        summary = "Renovar token",
        description = "Gera novos tokens a partir de um refresh token válido"
    )
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


    @Operation(
        summary = "Recuperar senha",
        description = "Gera token de recuperação e simula envio por email"
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        passwordRecoveryService.forgotPassword(request.getEmail());

        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Resetar senha",
        description = "Valida token e atualiza a senha do usuário"
    )
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
