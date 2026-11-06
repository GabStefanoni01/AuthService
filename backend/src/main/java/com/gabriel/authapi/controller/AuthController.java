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
@Tag(name = "Authentication", description = "Cadastro, login, tokens e recuperação de senha.")
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

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuário", description = "Cria uma conta com perfil USER e senha armazenada com BCrypt.")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Valida as credenciais e retorna access token e refresh token.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Realizar logout", description = "Revoga o refresh token informado.")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        refreshTokenService.revokeByToken(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens", description = "Revoga o refresh token atual e emite um novo par de tokens.")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.validate(request.getRefreshToken());
        User user = refreshToken.getUser();

        refreshTokenService.revoke(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.create(user, newRefreshToken);

        return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperação de senha", description = "Solicita um token de recuperação para o e-mail informado.")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordRecoveryService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Redefinir senha", description = "Redefine a senha usando o token de recuperação.")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordRecoveryService.resetPassword(
            request.getToken(),
            request.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}
