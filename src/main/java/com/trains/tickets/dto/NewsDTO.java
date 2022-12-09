package com.trains.tickets.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewsDTO {
    private Integer id;
    private String header;
    private String body;
    private LocalDate date;
    private String user;
    private boolean selected;
}
