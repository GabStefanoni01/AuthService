package com.gabriel.authapi.controller;

import java.security.Principal;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Account", description = "Endpoints que exigem autenticação.")
public class AccountController {

    @GetMapping("/me")
    @Operation(
        summary = "Consultar usuário autenticado",
        description = "Retorna o subject (e-mail) presente no access token JWT.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public Map<String, Object> currentUser(Principal principal) {
        return Map.of(
            "authenticated", true,
            "email", principal.getName()
        );
    }
}
