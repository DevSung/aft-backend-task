package com.example.domain.payload.user.response;

public record LoginResponse(
        String name,
        String userId,
        String accessToken) {

}
