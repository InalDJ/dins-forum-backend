package com.java.springportfolio.controller;

import com.java.springportfolio.dto.AuthenticationResponse;
import com.java.springportfolio.dto.LoginRequest;
import com.java.springportfolio.dto.RefreshTokenRequest;
import com.java.springportfolio.dto.RegisterRequest;
import com.java.springportfolio.exception.PortfolioException;
import com.java.springportfolio.service.AuthService;
import com.java.springportfolio.service.RefreshTokenService;
import com.java.springportfolio.util.DtoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.java.springportfolio.util.DtoValidator.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final DtoValidator dtoValidator;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) throws PortfolioException {
        dtoValidator.validateRegisterRequest(registerRequest);
        authService.signUp(registerRequest);
        return new ResponseEntity<>("User registration successful!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        dtoValidator.validateLoginRequest(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        dtoValidator.validateRefreshTokenRequest(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable Long id) {
        authService.deleteUser(id);
        return new ResponseEntity<>("Account has been deleted successfully", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        dtoValidator.validateRefreshTokenRequest(refreshTokenRequest);
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token has been deleted successfully!!");
    }
}
