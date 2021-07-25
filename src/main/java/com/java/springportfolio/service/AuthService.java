package com.java.springportfolio.service;

import com.java.springportfolio.dto.AuthenticationResponse;
import com.java.springportfolio.dto.LoginRequest;
import com.java.springportfolio.dto.RefreshTokenRequest;
import com.java.springportfolio.dto.RegisterRequest;
import com.java.springportfolio.entity.User;

public interface AuthService {

    void signUp(RegisterRequest registerRequest);

    AuthenticationResponse login(LoginRequest loginRequest);

    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    User getCurrentUser();

    boolean isLoggedIn();

    void verifyAccount(String verificationToken);

    void fetchUserAndEnable(String verificationToken);

    void deleteUser(Long id);
}
