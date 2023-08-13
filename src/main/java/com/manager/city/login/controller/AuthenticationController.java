package com.manager.city.login.controller;

import com.manager.city.login.domain.Role;
import com.manager.city.login.domain.RoleType;
import com.manager.city.login.domain.User;
import com.manager.city.login.domain.UserDetailsImpl;
import com.manager.city.login.dto.LoginRequest;
import com.manager.city.login.dto.RegistrationRequest;
import com.manager.city.login.repository.RoleRepository;
import com.manager.city.login.repository.UserRepository;
import com.manager.city.login.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
    public String register(@RequestBody RegistrationRequest request) {
        // TODO Check if user already exists
        // TODO Check email
        // TODO Check password
        // TODO Check enpty values
        // TODO Add roles to the users
        User user = new User(request.email(), passwordEncoder.encode(request.password()), request.name(), request.surname(), List.of());
        Optional<Role> citizenRole = roleRepository.findByName(RoleType.CITIZEN);
        citizenRole.ifPresent(role-> user.setRoles(List.of(role)));
        userRepository.save(user);
        return "MOCK: You have been registered with email [" + request.email() + "]!";
    }}
