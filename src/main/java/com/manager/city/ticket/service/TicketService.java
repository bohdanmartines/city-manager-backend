package com.manager.city.ticket.service;

import com.manager.city.login.domain.User;
import com.manager.city.login.exception.ApplicationException;
import com.manager.city.login.repository.UserRepository;
import com.manager.city.ticket.domain.Status;
import com.manager.city.ticket.domain.StatusType;
import com.manager.city.ticket.domain.Ticket;
import com.manager.city.ticket.dto.CreateTicketDto;
import com.manager.city.ticket.dto.TicketDto;
import com.manager.city.ticket.repository.StatusRepository;
import com.manager.city.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, StatusRepository statusRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    public Ticket createTicket(CreateTicketDto ticketRequest, long creatorId) {
        Optional<Status> openStatus = statusRepository.findByName(StatusType.OPEN);
        Optional<User> creator = userRepository.findById(creatorId);
        if (openStatus.isPresent() && creator.isPresent()) {
            Ticket ticket = new Ticket(ticketRequest.title(), ticketRequest.description(), creator.get(), openStatus.get());
            return ticketRepository.save(ticket);
        } else {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot initialise ticket status or creator");
        }
    }

    public TicketDto getTicket(long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        return ticket.map(TicketDto::toDto).get();
    }

    public Page<TicketDto> getTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ticketRepository.findAll(pageable).map(TicketDto::toDto);
    }
}
