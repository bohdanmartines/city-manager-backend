package com.manager.city.login.service;

public enum RegistrationStatus {
    OK("Registration successful"),
    NO_EMAIL("Please specify email"),
    INVALID_EMAIL("Please specify valid email"),
    NO_PASSWORD("Please specify password"),
    NO_PASSWORD_CONFIRMATION("Please specify password confirmation"),
    PASSWORD_CONFIRMATION_DIFFERENCE("Password must match password confirmation"),
    NO_NAME("Please specify name"),
    NO_SURNAME("Please specify surname");

    private final String message;

    RegistrationStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
