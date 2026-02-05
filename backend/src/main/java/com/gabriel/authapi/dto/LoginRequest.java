package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para autenticação do usuário")
public class LoginRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(
        description = "Email cadastrado do usuário",
        example = "gabriel@email.com",
        required = true
    )
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(
        description = "Senha do usuário",
        example = "MinhaSenha@123",
        required = true
    )
    private String password;
    
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
