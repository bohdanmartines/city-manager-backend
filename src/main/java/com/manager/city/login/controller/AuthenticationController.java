package com.manager.city.login.controller;

import com.manager.city.login.dto.LoginRequest;
import com.manager.city.login.dto.RegistrationRequest;
import com.manager.city.login.service.JwtService;
import com.manager.city.login.service.RegistrationService;
import com.manager.city.login.service.RegistrationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RegistrationService registrationService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.registrationService = registrationService;
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        ResponseCookie cookie = jwtService.generateJwtCookie(authentication.getName());
        LOGGER.info("User [" + authentication.getName() + "] logged in");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Successful login!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Principal principal) {
        LOGGER.info("Logging out user [" + principal.getName() + "]");
        ResponseCookie cookie = jwtService.generateClearJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Successful logout!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            RegistrationStatus status = registrationService.register(request);
            LOGGER.info("Registered user [" + request.email().trim() + "]");
            return ResponseEntity.ok().body(status.getMessage());
        } catch (Exception e) {
            LOGGER.error("Failed to register user: ", e);
            return ResponseEntity.internalServerError().body("Unexpected error during registration");
        }
    }


}
