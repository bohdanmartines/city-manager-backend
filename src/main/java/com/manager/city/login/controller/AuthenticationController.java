package com.manager.city.login.controller;

import com.manager.city.login.dto.LoginRequest;
import com.manager.city.login.dto.RegistrationRequest;
import com.manager.city.login.dto.TokenRefreshRequest;
import com.manager.city.login.dto.UserDto;
import com.manager.city.login.service.JwtService;
import com.manager.city.login.service.LoginService;
import com.manager.city.login.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final JwtService jwtService;
    private final LoginService loginService;
    private final RegistrationService registrationService;

    public AuthenticationController(JwtService jwtService, LoginService loginService, RegistrationService registrationService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    /**
     * @return response entity with refresh token as body and with a header to set access token to cookies
     */
    @PostMapping("login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok()
                .body(loginService.loginUser(request.email(), request.password()));
    }

    @GetMapping("logout")
    public ResponseEntity<String> logout(Principal principal) {
        LOGGER.info("Logging out user [" + principal.getName() + "]");
        ResponseCookie cookie = jwtService.generateClearAccessCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Successful logout!");
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody RegistrationRequest request) {
        UserDto user = registrationService.register(request);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("refresh")
    public ResponseEntity<String> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.token();
        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        String email = jwtService.getEmailFromJwt(refreshToken);
        String accessToken = jwtService.generateAccessToken(email);
        LOGGER.info("Refreshed access token for user [" + email + "]");
        return ResponseEntity.ok().body(accessToken);
    }
}
