package com.gabriel.authapi.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 min
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 dias

    public String generateAccessToken(String subject) {
        return generateToken(subject, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(String subject, long expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}