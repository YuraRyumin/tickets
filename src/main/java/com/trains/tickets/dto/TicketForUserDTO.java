package com.trains.tickets.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TicketForUserDTO {
    private String name;
    private String surname;
    private String passport;
    private LocalDate dateOfBirth;
    private LocalDateTime dateTicket;
    private String gender;
    private String trainNumber;
    private String wagonNumber;
    private Integer seat;
    private String serviceClass;
    private Integer price;
}
