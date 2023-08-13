package com.manager.city.login.dto;

public record RegistrationRequest(String email, String password, String confirmPassword, String name, String surname) {
}
