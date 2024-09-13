package com.manager.city.login.service;

import com.manager.city.login.domain.Role;
import com.manager.city.login.domain.RoleType;
import com.manager.city.login.domain.User;
import com.manager.city.login.dto.RegistrationRequest;
import com.manager.city.login.repository.RoleRepository;
import com.manager.city.login.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RegistrationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegistrationStatus register(RegistrationRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            return RegistrationStatus.NO_EMAIL;
        }
        if (!EmailValidator.getInstance().isValid(request.email())) {
            return RegistrationStatus.INVALID_EMAIL;
        }

        boolean userExists = userRepository.existsByEmail(request.email().trim());
        if (userExists) {
            return RegistrationStatus.INVALID_EMAIL;
        }

        if (request.password() == null || request.password().isBlank()) {
            return RegistrationStatus.NO_PASSWORD;
        }
        if (request.confirmPassword() == null || request.confirmPassword().isBlank()) {
            return RegistrationStatus.NO_PASSWORD_CONFIRMATION;
        }
        if (!request.password().equals(request.confirmPassword())) {
            return RegistrationStatus.PASSWORD_CONFIRMATION_DIFFERENCE;
        }

        if (request.name() == null || request.name().isBlank()) {
            return RegistrationStatus.NO_NAME;
        }
        if (request.surname() == null || request.surname().isBlank()) {
            return RegistrationStatus.NO_SURNAME;
        }

        User user = new User(request.email().trim(), passwordEncoder.encode(request.password().trim()), request.name().trim(), request.surname().trim());
        Optional<Role> citizenRole = roleRepository.findByName(RoleType.CITIZEN);
        citizenRole.ifPresent(role-> user.setRoles(List.of(role)));
        userRepository.save(user);

        return RegistrationStatus.OK;
    }
}
