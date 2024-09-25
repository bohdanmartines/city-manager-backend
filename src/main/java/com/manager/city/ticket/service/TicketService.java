package com.manager.city.ticket.service;

import com.manager.city.login.exception.ApplicationException;
import com.manager.city.ticket.domain.Status;
import com.manager.city.ticket.domain.StatusType;
import com.manager.city.ticket.domain.Ticket;
import com.manager.city.ticket.dto.CreateTicketDto;
import com.manager.city.ticket.dto.TicketDto;
import com.manager.city.ticket.repository.StatusRepository;
import com.manager.city.ticket.repository.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StatusRepository statusRepository;

    public TicketService(TicketRepository ticketRepository, StatusRepository statusRepository) {
        this.ticketRepository = ticketRepository;
        this.statusRepository = statusRepository;
    }

    public Ticket createTicket(CreateTicketDto ticketRequest, long creatorId) {
        Optional<Status> openStatus = statusRepository.findByName(StatusType.OPEN);
        if (openStatus.isPresent()) {
            Ticket ticket = new Ticket(ticketRequest.title(), ticketRequest.description(), creatorId, openStatus.get());
            return ticketRepository.save(ticket);
        } else {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot initialise ticket status");
        }
    }

    public TicketDto getTicket(long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        return ticket.map(TicketDto::toDto).get();
    }

    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketDto::toDto)
                .toList();
    }
}
