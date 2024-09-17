package com.manager.city.login.service;

import com.manager.city.login.domain.Role;
import com.manager.city.login.domain.RoleType;
import com.manager.city.login.domain.User;
import com.manager.city.login.dto.RegistrationRequest;
import com.manager.city.login.dto.UserDto;
import com.manager.city.login.exception.ApplicationException;
import com.manager.city.login.repository.RoleRepository;
import com.manager.city.login.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public RegistrationService(UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder,
                               JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserDto register(RegistrationRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.NO_EMAIL.getMessage());
        }
        if (!EmailValidator.getInstance().isValid(request.email())) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.INVALID_EMAIL.getMessage());
        }

        boolean userExists = userRepository.existsByEmail(request.email().trim());
        if (userExists) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.INVALID_EMAIL.getMessage());
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.NO_PASSWORD.getMessage());
        }
        if (request.confirmPassword() == null || request.confirmPassword().isBlank()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.NO_PASSWORD_CONFIRMATION.getMessage());
        }
        if (!request.password().equals(request.confirmPassword())) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.PASSWORD_CONFIRMATION_DIFFERENCE.getMessage());
        }

        if (request.name() == null || request.name().isBlank()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.NO_NAME.getMessage());
        }
        if (request.surname() == null || request.surname().isBlank()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, RegistrationStatus.NO_SURNAME.getMessage());
        }

        User user = new User(request.email().trim(), passwordEncoder.encode(request.password().trim()), request.name().trim(), request.surname().trim());
        Optional<Role> citizenRole = roleRepository.findByName(RoleType.CITIZEN);
        citizenRole.ifPresent(role-> user.setRoles(List.of(role)));
        User savedUser = userRepository.save(user);
        LOGGER.info("Registered user [" + savedUser.getEmail() + "]");

        String accessToken = jwtService.generateAccessToken(savedUser.getEmail());
        String refreshToken = jwtService.generateRefreshToken(savedUser.getEmail());
        List<String> roles = savedUser.getRoles().stream().map(role -> role.getName().name()).toList();
        return new UserDto(savedUser.getId(), savedUser.getEmail(), roles, accessToken, refreshToken);
    }
}
