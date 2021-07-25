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
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                           MailService mailService, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.mailService = mailService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void signUp(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new PortfolioException("User with the email: " + registerRequest.getEmail() + " already exists!");
        } else {
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
        System.out.println(authenticationResponse.getUsername());
        System.out.println(authenticationResponse.getAuthenticationToken());
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticationToken(token);
        authenticationResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
        authenticationResponse.setExpiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()));
        authenticationResponse.setUsername(refreshTokenRequest.getUsername());
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
    }

    @Override
    @Transactional(noRollbackFor = PortfolioException.class)
    public void fetchUserAndEnable(String verificationToken) {
        User user = userRepository.findByVerificationToken(verificationToken).orElseThrow(() -> new ItemNotFoundException("User has not been found"));

        //check if signup token has expired or not (25 min)
        if (Instant.now().toEpochMilli() - user.getCreated().toEpochMilli() > 15000000_0) {
            System.out.println(user.getUserId());
            userRepository.deleteById(user.getUserId());
            throw new PortfolioException("The verification token has expired!");
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
