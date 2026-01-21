package com.gabriel.authapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gabriel.authapi.dto.LoginRequest;
import com.gabriel.authapi.dto.LoginResponse;
import com.gabriel.authapi.dto.RefreshTokenRequest;
import com.gabriel.authapi.dto.RegisterRequest;
import com.gabriel.authapi.dto.UserResponse;
import com.gabriel.authapi.domain.entity.RefreshToken;
import com.gabriel.authapi.domain.entity.User;
import com.gabriel.authapi.security.jwt.JwtService;
import com.gabriel.authapi.service.AuthService;
import com.gabriel.authapi.service.RefreshTokenService;
import com.gabriel.authapi.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            AuthService authService,
            RefreshTokenService refreshTokenService,
            JwtService jwtService) {

        this.userService = userService;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.validate(request.getRefreshToken());

        User user = refreshToken.getUser();

        // revoga o refresh token atual
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
}
