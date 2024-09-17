package com.manager.city.mock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mock")
public class MockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockController.class);

    @GetMapping("messages")
    public ResponseEntity<List<String>> messages() {
        LOGGER.info("Returning mock messages");
        return ResponseEntity.ok()
                .body(List.of("Message 1", "Message 2", "Message 3"));
    }
}
