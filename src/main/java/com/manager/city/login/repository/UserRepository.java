package com.manager.city.login.repository;

import com.manager.city.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String username);
    Optional<User> findByEmail(String username);
}
