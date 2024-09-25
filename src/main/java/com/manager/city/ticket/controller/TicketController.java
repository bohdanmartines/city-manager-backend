package com.manager.city.ticket.controller;

import com.manager.city.ticket.dto.TicketDto;
import com.manager.city.ticket.dto.CreateTicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

    private static final List<TicketDto> MOCK_TICKETS = List.of(
            new TicketDto(1, "Install More Public Benches", "Request to install more benches in the city parks for elderly and families.", "Open", "2024-09-23T10:00:00Z"),
            new TicketDto(2, "Improve Street Lighting", "Several streets in the downtown area need better lighting for safety.", "In Progress", "2024-09-22T12:30:00Z"),
            new TicketDto(3, "Increase Green Spaces", "Proposal to convert unused lots into community gardens and green spaces.", "Closed", "2024-09-21T14:45:00Z")
    );

    @GetMapping
    public ResponseEntity<List<TicketDto>> messages() {
        return ResponseEntity.ok().body(MOCK_TICKETS);
    }

    @PostMapping("new")
    public ResponseEntity<Long> createTicket(@RequestBody CreateTicketDto ticketDto) throws URISyntaxException {
        LOGGER.info("Received a call to create a ticket [{}]", ticketDto);
        return ResponseEntity.created(URI.create("/api/ticket/" + 1)).body(1L);
    }
}
