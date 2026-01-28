package com.gabriel.authapi.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.gabriel.authapi.config.AuthConstants;
import com.gabriel.authapi.domain.entity.User;
import com.gabriel.authapi.repository.UserRepository;

@Service
public class LoginAttemptService {

    private final UserRepository userRepository;

    public LoginAttemptService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Quando login falha
    public void loginFailed(User user) {

        int attempts = user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);

        if (attempts >= AuthConstants.MAX_LOGIN_ATTEMPTS) {

            user.setLockUntil(
                    LocalDateTime.now()
                            .plusMinutes(AuthConstants.LOCK_MINUTES)
            );
        }

        userRepository.save(user);
    }

    // Quando login dá certo
    public void loginSucceeded(User user) {

        user.setFailedLoginAttempts(0);
        user.setLockUntil(null);

        userRepository.save(user);
    }

    // Verifica se está bloqueado
    public boolean isBlocked(User user) {

        if (user.getLockUntil() == null) return false;

        if (user.getLockUntil().isBefore(LocalDateTime.now())) {
            user.setLockUntil(null);
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            return false;
        }

        return true;
    }
}
