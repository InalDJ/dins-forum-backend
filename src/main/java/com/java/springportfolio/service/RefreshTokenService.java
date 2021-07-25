package com.java.springportfolio.service;

import com.java.springportfolio.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken();

    void validateRefreshToken(String token);

    void deleteRefreshToken(String token);
    }
