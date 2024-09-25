package com.manager.city.ticket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketController {

    private static final List<TicketDto> MOCK_TICKETS = List.of(
            new TicketDto(1, "Install More Public Benches", "Request to install more benches in the city parks for elderly and families.", "Open", "2024-09-23T10:00:00Z"),
            new TicketDto(2, "Improve Street Lighting", "Several streets in the downtown area need better lighting for safety.", "In Progress", "2024-09-22T12:30:00Z"),
            new TicketDto(3, "Increase Green Spaces", "Proposal to convert unused lots into community gardens and green spaces.", "Closed", "2024-09-21T14:45:00Z")
    );

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/ticket/get")
    public ResponseEntity<List<TicketDto>> messages() {
        return ResponseEntity.ok().body(MOCK_TICKETS);
    }
}
