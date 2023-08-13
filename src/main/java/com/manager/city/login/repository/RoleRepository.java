package com.manager.city.login.repository;

import com.manager.city.login.domain.Role;
import com.manager.city.login.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
