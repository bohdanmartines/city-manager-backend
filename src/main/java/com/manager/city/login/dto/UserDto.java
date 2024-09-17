package com.manager.city.login.dto;

import java.util.List;

public record UserDto(long id, String email, List<String> roles, String accessToken, String refreshToken) {
}
