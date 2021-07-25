package com.java.springportfolio.service;

import com.java.springportfolio.dao.RefreshTokenRepository;
import com.java.springportfolio.entity.RefreshToken;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.java.springportfolio.security.JwtProvider.isTokenExpired;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken() {
        log.info("Generating a refresh token...");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(String token) {
        log.info("Validating refresh token...");
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ItemNotFoundException("Token has not been found!"));
        if (isTokenExpired(refreshToken.getCreatedDate().toEpochMilli(), 520_000_000)) {
            log.error("The refresh token has been expired!");
            throw new PortfolioException("The refresh token has expired!");
        }
        log.info("The refresh token has been validated!");
    }

    @Transactional
    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        log.info("Refresh token has been removed from the database!");
    }
}
