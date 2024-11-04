package com.example.auth.jwt;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JwtToken {
    private String grantType;
    private String accessToken;
}
