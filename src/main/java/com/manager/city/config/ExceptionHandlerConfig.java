package com.manager.city.config;

import com.manager.city.login.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public ResponseEntity<String> handleApplicationException(ApplicationException ex) {
        LOGGER.error("Got an application exception", ex);
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception ex) {
        LOGGER.error("Got an unexpected exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
