package com.example.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(getMessage(), status.value());
    }

    public record ErrorResponse(String message, int statusCode) {
    }
}