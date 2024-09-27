package com.manager.city.ticket.dto;

import com.manager.city.ticket.domain.Ticket;

public record TicketDto(long id, String title, String description, String status, String creatorEmail, String assigneeEmail, String createdAt) {

    public static TicketDto toDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus().getName().name(),
                ticket.getCreator().getEmail(),
                ticket.getAssignee() != null ? ticket.getAssignee().getEmail() : null,
                ticket.getCreatedAt() != null ? ticket.getCreatedAt().toString() : null
        );
    }
}
