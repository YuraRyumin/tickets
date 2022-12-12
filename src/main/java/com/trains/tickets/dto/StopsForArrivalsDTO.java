package com.trains.tickets.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StopsForArrivalsDTO {
    private String station;
    private String schedule;
    private LocalTime timeBegining;
    private LocalTime timeEnd;
    private String train;
}
