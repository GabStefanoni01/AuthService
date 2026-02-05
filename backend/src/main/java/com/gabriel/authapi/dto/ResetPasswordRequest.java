package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para redefinição de senha usando token de recuperação")
public class ResetPasswordRequest {

    @Schema(
        description = "Token temporário enviado para o e-mail do usuário",
        example = "c8f12a9b-4d21-4f8c-9f32-abc123def456"
    )
    private String token;

    @Schema(
        description = "Nova senha do usuário",
        example = "NovaSenha@123"
    )
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
