package com.warhammer.primarchs.auth.dto;

public record AuthResponse(String token, String username, String role) {
}
