package com.manager.city.vote.controller;

import com.manager.city.login.domain.UserDetailsImpl;
import com.manager.city.vote.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
public class VoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("{ticketId}/vote")
    public ResponseEntity<Void> voteTicket(Authentication authentication, @PathVariable long ticketId) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        LOGGER.info("User [{}] votes for ticket [{}]", user.getId(), ticketId);
        voteService.voteTicket(ticketId, user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("{ticketId}/unvote")
    public ResponseEntity<Void> unvoteTicket(Authentication authentication, @PathVariable long ticketId) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        LOGGER.info("User [{}] un-votes for ticket [{}]", user.getId(), ticketId);
        voteService.unvoteTicket(ticketId, user.getId());
        return ResponseEntity.ok().build();
    }
}
