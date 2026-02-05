package com.gabriel.authapi.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estrutura padrão de erro da API")
public class ErrorResponse {

    @Schema(
        description = "Data e hora do erro",
        example = "2026-02-05T18:30:00"
    )
    private LocalDateTime timestamp;

    @Schema(
        description = "Código HTTP do erro",
        example = "404"
    )
    private int status;

    @Schema(
        description = "Tipo do erro",
        example = "Not Found"
    )
    private String error;

    @Schema(
        description = "Mensagem detalhada do erro",
        example = "Usuário não encontrado"
    )
    private String message;

    @Schema(
        description = "Endpoint onde ocorreu o erro",
        example = "/auth/login"
    )
    private String path;

    public ErrorResponse(
            int status,
            String error,
            String message,
            String path
    ) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
