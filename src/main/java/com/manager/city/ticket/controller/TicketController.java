package com.manager.city.ticket.controller;

import com.manager.city.login.domain.UserDetailsImpl;
import com.manager.city.ticket.domain.Ticket;
import com.manager.city.ticket.dto.CreateTicketDto;
import com.manager.city.ticket.dto.TicketDto;
import com.manager.city.ticket.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<Page<TicketDto>> getAllTickets(@RequestParam int page, @RequestParam int size) {
        LOGGER.info("Received request to get all tickets with pagination params: page [{}], size [{}]", page, size);
        return ResponseEntity.ok()
                .body(ticketService.getTickets(page, size));
    }

    @PostMapping("new")
    public ResponseEntity<Long> createTicket(Authentication authentication,
                                             @RequestBody CreateTicketDto ticketDto) {
        LOGGER.info("Received a call to create a ticket [{}]", ticketDto);
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Ticket ticket = ticketService.createTicket(ticketDto, user.getId());
        Long ticketId = ticket.getId();
        return ResponseEntity.created(URI.create("/api/ticket/" + ticketId)).body(ticketId);
    }

    @GetMapping("{ticketId}")
    public ResponseEntity<TicketDto> getTicket(Authentication authentication,
                                               @PathVariable long ticketId) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        TicketDto ticket = ticketService.getTicket(ticketId, user.getId());
        return ResponseEntity.ok().body(ticket);
    }

    @PostMapping("{ticketId}/vote")
    public ResponseEntity<Void> voteForTicket(Authentication authentication,
                                               @PathVariable long ticketId) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        LOGGER.info("User [{}] votes for ticket [{}]", user.getId(), ticketId);
        ticketService.voteForTicket(ticketId, user.getId());
        return ResponseEntity.ok().build();
    }
}
