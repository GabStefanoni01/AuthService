package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para cadastro de novo usuário")
public class RegisterRequest {

    @Schema(
        description = "Nome completo do usuário",
        example = "Gabriel Stefanoni"
    )
    private String name;

    @Schema(
        description = "E-mail do usuário (deve ser único)",
        example = "gabriel@email.com"
    )
    private String email;

    @Schema(
        description = "Senha do usuário (mínimo 8 caracteres recomendado)",
        example = "Senha@123"
    )
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
