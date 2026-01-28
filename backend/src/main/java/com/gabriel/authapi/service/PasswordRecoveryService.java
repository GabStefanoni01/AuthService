package com.gabriel.authapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gabriel.authapi.domain.entity.PasswordResetToken;
import com.gabriel.authapi.domain.entity.User;
import com.gabriel.authapi.repository.PasswordResetTokenRepository;
import com.gabriel.authapi.repository.UserRepository;

@Service
public class PasswordRecoveryService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final long TOKEN_MINUTES = 30;

    public PasswordRecoveryService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository) {

        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    // Enviar link
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(
                LocalDateTime.now().plusMinutes(TOKEN_MINUTES)
        );

        tokenRepository.save(resetToken);

        // Simulação de email
        System.out.println("==== PASSWORD RESET ====");
        System.out.println("User: " + email);
        System.out.println("Token: " + token);
        System.out.println("Link: http://localhost:8080/reset?token=" + token);
        System.out.println("========================");
    }

    // Resetar senha
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
