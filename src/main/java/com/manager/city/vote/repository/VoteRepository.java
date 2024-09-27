package com.manager.city.vote.repository;

import com.manager.city.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByTicketIdAndVoterId(long ticketId, long voterId);
    void deleteByTicketIdAndVoterId(long ticketId, long voterId);
    int countByTicketId(long ticketId);
}
