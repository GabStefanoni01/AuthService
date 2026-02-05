package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para gerar um novo access token usando refresh token")
public class RefreshTokenRequest {

    @Schema(
        description = "Refresh token válido para renovação da sessão",
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
