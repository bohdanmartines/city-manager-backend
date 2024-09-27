package com.manager.city.vote.service;

import com.manager.city.login.exception.ApplicationException;
import com.manager.city.login.repository.UserRepository;
import com.manager.city.ticket.domain.Ticket;
import com.manager.city.ticket.repository.StatusRepository;
import com.manager.city.ticket.repository.TicketRepository;
import com.manager.city.vote.domain.Vote;
import com.manager.city.vote.repository.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

    private final TicketRepository ticketRepository;
    private final VoteRepository voteRepository;

    public VoteService(TicketRepository ticketRepository, StatusRepository statusRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.ticketRepository = ticketRepository;
        this.voteRepository = voteRepository;
    }

    public void voteTicket(long ticketId, long userId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        Vote vote = new Vote(ticketId, userId);
        voteRepository.save(vote);
    }

    @Transactional
    public void unvoteTicket(long ticketId, long userId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        voteRepository.deleteByTicketIdAndVoterId(ticketId, userId);
    }
}
