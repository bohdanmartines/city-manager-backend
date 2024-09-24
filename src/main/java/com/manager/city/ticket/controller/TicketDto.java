package com.manager.city.ticket.controller;

public record TicketDto(long id, String title, String description, String status, String createdAt) {
}
