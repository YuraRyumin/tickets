package com.trains.tickets.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketDTO {
    private Integer id;
    private String passenger;
    private LocalDate dateTicket;
    private String train;
    private String wagon;
    private Integer price;
    private String schedule;
}
