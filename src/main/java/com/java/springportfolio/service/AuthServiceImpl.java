package com.java.springportfolio.service;

import com.java.springportfolio.dao.UserRepository;
import com.java.springportfolio.dto.AuthenticationResponse;
import com.java.springportfolio.dto.LoginRequest;
import com.java.springportfolio.dto.RefreshTokenRequest;
import com.java.springportfolio.dto.RegisterRequest;
import com.java.springportfolio.entity.NotificationEmail;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.PortfolioException;
import com.java.springportfolio.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.java.springportfolio.security.JwtProvider.isTokenExpired;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    @Value("${jwt.expiration.time}")
    private long expirationMillis;

    @Override
    public void signUp(RegisterRequest registerRequest) {
        log.info("Registering user with email: '{}'...", registerRequest.getEmail());
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.error("User with email: '{}' already exists!", registerRequest.getEmail());
            throw new PortfolioException("User with email: '" + registerRequest.getEmail() + "' already exists!");
        } else {
            log.info("Email is unique. Proceeding to registration...");
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setCreated(Instant.now());
            user.setEnabled(false);
            user.setVerificationToken(generateVerificationToken());
            userRepository.save(user);
            mailService.sendMail(new NotificationEmail("Благодарим Вас за регистрацию на нашем сайте!",
                    user.getEmail(), " Пожалуйста, активируйте Ваш аккаунт, перейдя по ссылке: " + " http://localhost:8080/api/auth/accountVerification/" + user.getVerificationToken()));
            log.info("The user with email: '{}' has been saved to the database!", registerRequest.getEmail());
        }
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticationToken(token);
        authenticationResponse.setExpiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()));
        authenticationResponse.setRefreshToken(refreshTokenService.generateRefreshToken().getToken());
        authenticationResponse.setUsername(loginRequest.getUsername());
        log.info("User with name: '{}' has logged in successfully!", loginRequest.getUsername());
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("Refreshing token...");
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticationToken(token);
        authenticationResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
        authenticationResponse.setExpiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()));
        authenticationResponse.setUsername(refreshTokenRequest.getUsername());
        log.info("The token has been refreshed!");
        return authenticationResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername()).orElseThrow(() -> new ItemNotFoundException("User has not been found"));
    }

    @Override
    @Transactional(noRollbackFor = PortfolioException.class)
    public void verifyAccount(String verificationToken) {
        log.info("Verifying account...");
        fetchUserAndEnable(verificationToken);
    }

    @Override
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("The user with id: '{}' has been deleted!", id);
    }

    @Override
    @Transactional(noRollbackFor = PortfolioException.class)
    public void fetchUserAndEnable(String verificationToken) {
        User user = userRepository.findByVerificationToken(verificationToken).orElseThrow(() -> new ItemNotFoundException("User has not been found"));

        if (isTokenExpired(user.getCreated().toEpochMilli(), expirationMillis)) {
            userRepository.deleteById(user.getUserId());
            log.error("The verification token has been expired!");
            throw new PortfolioException("The verification token has expired!");
        }
        user.setEnabled(true);
        userRepository.save(user);
        log.info("The user with name: '{}' has been enabled!", user.getUsername());
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
