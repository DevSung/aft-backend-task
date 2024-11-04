package com.example.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private void logError(HttpStatus status, String message, Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        log.error("errorCode:{}, message:{}, at:\n{}", status.value(), message, sw);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomException.ErrorResponse> handleCustomException(CustomException ex) {
        logError(ex.getStatus(), ex.getMessage(), ex);
        return new ResponseEntity<>(ex.toErrorResponse(), ex.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomException.ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        CustomException.ErrorResponse errorResponse = new CustomException.ErrorResponse(
                "Json 형식 오류", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomException.ErrorResponse> handleException(Exception ex) {
        logError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        CustomException.ErrorResponse errorResponse = new CustomException.ErrorResponse(
                "서버 에러", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomException.ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        // Validation 에러를 수집
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String message = fieldError.getDefaultMessage();
            errorMessage.append(message).append(" ");
        });

        // 마지막 쉼표 및 공백 제거
        if (!errorMessage.isEmpty()) {
            errorMessage.setLength(errorMessage.length() - 2); // 쉼표와 공백 제거
        }

        CustomException.ErrorResponse errorResponse = new CustomException.ErrorResponse(
                errorMessage.toString(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}