package com.trains.tickets.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    private String code;
    private String message;
    private String body;
}
