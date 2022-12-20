package com.trains.tickets.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StopDTO {
    private Integer id;
    private String station;
    private String schedule;
    private LocalTime timeBegining;
    private LocalTime timeEnd;
}
