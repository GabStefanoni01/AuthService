package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para realizar logout e revogar o refresh token")
public class LogoutRequest {

    @Schema(
        description = "Refresh token que será revogado no logout",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}