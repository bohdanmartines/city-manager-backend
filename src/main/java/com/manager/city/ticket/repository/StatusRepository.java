package com.manager.city.ticket.repository;

import com.manager.city.ticket.domain.Status;
import com.manager.city.ticket.domain.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(StatusType name);
}
