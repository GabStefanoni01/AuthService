package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para recuperação de senha")
public class ForgotPasswordRequest {

    @Schema(
        description = "Email do usuário cadastrado no sistema",
        example = "gabriel@email.com",
        required = true
    )
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
