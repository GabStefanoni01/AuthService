package com.gabriel.authapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados públicos do usuário retornados pela API")
public class UserResponse {
    
    @Schema(
        description = "ID único do usuário no sistema",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Nome completo do usuário",
        example = "Gabriel Stefanoni"
    )
    private String name;

    @Schema(
        description = "E-mail do usuário",
        example = "gabriel@email.com"
    )
    private String email;

    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
