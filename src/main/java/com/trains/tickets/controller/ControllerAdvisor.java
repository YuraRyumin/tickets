package com.trains.tickets.controller;

import com.trains.tickets.exception.AuthException;
import com.trains.tickets.exception.NotFoundException;
import com.trains.tickets.exception.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

import static java.time.LocalDateTime.now;

@ControllerAdvice
@ResponseBody
//public class ControllerAdvisor {
//    private static final Logger LOG = LogManager.getLogger(ControllerAdvisor.class);
//
//    @ExceptionHandler(RuntimeException.class)
//    public String handleException(HttpServletRequest request, Exception e){
//        LOG.error(request.getRequestURL(), e);
//        return "/404";
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public String handleNoHandlerFoundException(NoHandlerFoundException e){
//        LOG.error(e);
//        return "404";
//    }
//}
public class ControllerAdvisor {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException validationException) {
        return ErrorResponse.builder()
                .message(validationException.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException notFoundException) {
        return ErrorResponse.builder()
                .message(notFoundException.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotFoundException(AuthException authException) {
        return ErrorResponse.builder()
                .message(authException.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(now())
                .build();
    }
}
