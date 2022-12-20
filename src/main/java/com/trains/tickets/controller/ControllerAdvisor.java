package com.trains.tickets.controller;

import com.trains.tickets.dto.ErrorResponse;
import com.trains.tickets.exception.AuthException;
import com.trains.tickets.exception.NotFoundException;
import com.trains.tickets.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.time.LocalDateTime.now;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ControllerAdvisor {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException validationException) {
        log.error(validationException.getMessage());
        return ErrorResponse.builder()
                .message(validationException.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException notFoundException) {
        log.error(notFoundException.getMessage());
        return ErrorResponse.builder()
                .message(notFoundException.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotFoundException(AuthException authException) {
        log.error(authException.getMessage());
        return ErrorResponse.builder()
                .message(authException.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(now())
                .build();
    }
}
